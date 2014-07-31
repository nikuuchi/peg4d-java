package org.peg4d;

import java.util.LinkedHashMap;
import java.util.Map;

class Stat {

	Stat(Grammar peg, ParserSource source) {
		this.PegSize = peg.pegList.size();
		source.stat = this;
	}
	
	int PegSize;
	int statOptimizedPeg = 0;

	long statFileLength = 0;  /* [bytes] */
	long statIOCount    = 0;
	long statReadLength = 0;  /* [bytes] */
	
	final void readFile(int length) {
		this.statIOCount += 1;
		this.statReadLength += length;
	}

	long UnconsumedLength = 0; /* [chars] */
	long ConsumedLength = 0;   /* [chars] */

	long BacktrackCount = 0;
	long BacktrackSize = 0;
	
	long WorstBacktrackSize = 0;
	long WorstPosition = 0;

	public final void statBacktrack(long pos, long len) {
		this.BacktrackCount = this.BacktrackCount + 1;
		this.BacktrackSize  = this.BacktrackSize + len;
		this.countBacktrackLength(len);
		if(len > this.WorstBacktrackSize) {
			this.WorstBacktrackSize = len;
			this.WorstPosition = pos;
		}
		if(pos < BacktrackHistgram.length) {
			setHistgram((int)pos, (int)len);
		}
	}

	int backtrackCount[] = new int[32];
	private void countBacktrackLength(long len) {
		int n = (int)(Math.log(len) / Math.log(2.0));
		backtrackCount[n] += 1;
	}
	
	int BacktrackHistgram[] = new int[4096];
	
	private void setHistgram(int pos, int len) {
		int end = pos + len < BacktrackHistgram.length ? pos + len : BacktrackHistgram.length;
		for(int i = pos; i < end; i++) {
			this.BacktrackHistgram[i] += 1;
		}
	}

	private void showHistgram() {
		int max = 1;
		for(int i = 0; i < this.BacktrackHistgram.length; i++) {
			if(max < this.BacktrackHistgram[i]) {
				max = this.BacktrackHistgram[i];
			}
		}
		for(int i = 0; i < this.BacktrackHistgram.length/10; i++) {
			System.out.println(""+i + "\t: " + this.BacktrackHistgram[i] + "\t" + star(this.BacktrackHistgram[i], max));
		}
	}

	private String star(int n, int m) {
		n = 30 * n / m;
		String s = "";
		for(int i = 0; i < n; i++) {
			s += "*";
		}
		return s;
	}
	
	int  NewObjectCount = 0;
	int  ObjectCount    = 0;
	int  UsedObjectCount = 0;
	int  DisposalObjectCount = 0;
	int  NodeCount = 0;
	int  EdgeCount = 0;
	int  ObjectMaxDepth = 0;
	long HeapSize = 0;
	long UsedHeapSize = 0;
	
	final void countObjectCreation() {
		this.ObjectCount += 1;
	}
	
	final void statObject(Pego pego) {
		UMap<ObjectCounter> m = new UMap<ObjectCounter>();
		this.UsedObjectCount = 0;
		this.EdgeCount = 0;
		this.NodeCount = 0;
		this.ObjectMaxDepth = 0;
		
		this.statObjectImpl(pego, 1, m);
		
		this.setCount("CreatedObject", this.ObjectCount);
		this.setCount("UsedObject", this.UsedObjectCount);
		this.setCount("DisposedObject", this.ObjectCount - this.UsedObjectCount);
		this.setRatio("Disposal/Used", this.ObjectCount - this.UsedObjectCount, this.UsedObjectCount);
		this.setCount("NewObject", this.NewObjectCount);
		this.setRatio("New/Creation", this.NewObjectCount - this.ObjectCount, this.ObjectCount);
		this.setCount("ObjectEdge", this.EdgeCount);
		this.setCount("ObjectNode", this.NodeCount);
		this.setCount("ObjectDepth", this.ObjectMaxDepth);

//		int DataSize = 0;
//		ObjectCounter data = null;
//		UList<String> keys = m.keys();
//		for(int i = 0; i < keys.size(); i++) {
//			ObjectCounter c = m.get(keys.ArrayValues[i]);
//			if(DataSize < c.count && c.size > 0) {
//				data = c;
//			}
//		}
//		this.setText("DataObject", data.tag);
//		this.setCount("DataSize", data.count);
//		this.setRatio("DataLength", data.length, data.count);
	}

	private void statObjectImpl(Pego pego, int depth, UMap<ObjectCounter> m) {
		if(depth > this.ObjectMaxDepth) {
			this.ObjectMaxDepth = depth;
		}
		this.UsedObjectCount += 1;
		if(pego.size() == 0) {
			this.EdgeCount += 1;
		}
		else {
			this.NodeCount += 1;
			for(int i = 0; i < pego.size(); i++) {
				this.statObjectImpl(pego.get(i), depth+1, m);
			}
		}
		String tag = pego.getTag();
		ObjectCounter c = m.get(tag);
		if(c == null) {
			c = new ObjectCounter();
			c.tag = tag;
			m.put(tag, c);
		}
		c.count += 1;
		c.length += pego.getLength();
		c.size += pego.size();
	}
	
	class ObjectCounter {
		String tag;
		int count = 0;
		long length = 0;
		int size = 0;
	}
	
	private final static int MapFifoSize = 9999;
	Map<Long, Peg> repeatMap = null;
	private long lastestEntryPosition = 0;
	private int MinimumStoredLength = Integer.MAX_VALUE;
	
	private int CallCount = 0;
	private int RepeatCount = 0;

	int[] callCount = null;
	int[] repeatCount = null;

	void initRepeatCounter() {
		this.CallCount = 0;
		this.RepeatCount = 0;
		MinimumStoredLength = Integer.MAX_VALUE;
		this.repeatMap = new LinkedHashMap<Long, Peg>(MapFifoSize) {
			private static final long serialVersionUID = 6725894996600788028L;
			@Override
			protected boolean removeEldestEntry(Map.Entry<Long, Peg> eldest)  {
				if(this.size() > MapFifoSize) {
					long pos = PEGUtils.getpos(eldest.getKey());
					int delta = (int)(lastestEntryPosition - pos);
					if(delta < MinimumStoredLength) {
						MinimumStoredLength = delta;
					}
					return true;			
				}
				return false;
			}
		};
		this.callCount = new int[PegSize+1];
		this.repeatCount = new int[PegSize+1];
	}

	final void countRepeatCall(Peg e, long pos) {
		this.NewObjectCount += 1;
		this.CallCount += 1;
		if(this.callCount != null) {
			callCount[e.uniqueId] += 1;
			Long key = PEGUtils.objectId(pos, e);
			Peg p = this.repeatMap.get(key);
			if(p != null) {
				assert(p == e);
				RepeatCount += 1;
				repeatCount[e.uniqueId] += 1;
				//System.out.println("pos="+pos + ", " + repeatCount[e.uniqueId] + " e= " + e.uniqueId + ", " + e);
			}
			else {
				if(pos > this.lastestEntryPosition) {
					this.lastestEntryPosition = pos;
				}
				this.repeatMap.put(key, e);
			}
		}
	}

	final void ckeckRepeatCounter() {
		if(this.repeatMap != null) {
			this.setCount("Calls", this.CallCount);
			this.setCount("Repeats", this.RepeatCount);
			this.setRatio("Repeats/Calls", this.RepeatCount, this.CallCount);
			this.setCount("EnsuredBacktrack", this.MinimumStoredLength);		
		}
	}

	
	long ErapsedTime = 0;
	double Latency  = 0;

	public void start() {
		System.gc(); // meaningless ?
		long total = Runtime.getRuntime().totalMemory();
		long free =  Runtime.getRuntime().freeMemory();
		this.UsedHeapSize =  total - free;
		this.ErapsedTime = System.currentTimeMillis();
	}
	
	public void end(Pego pego, ParserContext p) {
		this.ErapsedTime = (System.currentTimeMillis() - ErapsedTime);

		System.gc(); // meaningless ?
		this.ConsumedLength = p.getPosition();
		this.UnconsumedLength = p.endPosition - p.getPosition();
		this.statFileLength = p.source.getFileLength();

		long total = Runtime.getRuntime().totalMemory();
		long free =  Runtime.getRuntime().freeMemory();
		this.HeapSize = total - free;
		this.UsedHeapSize =  this.HeapSize - this.UsedHeapSize;

		this.set(new vText("Parser", p.getName()));
		this.setCount("OptimizationLevel", Main.OptimizationLevel);
		this.setCount("MemoFactor", Main.MemoFactor);

		this.set(new vText("FileName", p.source.fileName));
		this.setCount1("FileSize", this.statFileLength);
		this.setCount1("DiskIO", this.statIOCount);
		this.setCount1("ReadFileSize", this.statReadLength);
		this.setRatio1("IO Ratio", this.statReadLength, this.statFileLength);

		this.setCount("ConsumedLength", this.ConsumedLength);
		this.setCount1("UnconsumedLength", this.UnconsumedLength);
		
		this.setCount("BacktrackLength", this.BacktrackSize);
		this.setRatio("Backtrack/Consumed", this.BacktrackSize, this.ConsumedLength);
		
		this.setCount("Backtrack", this.BacktrackCount);
		this.setCount("WorstBacktrack", this.WorstBacktrackSize);
		this.setRatio("BacktrackAverage", this.BacktrackSize, this.BacktrackCount);
		this.setRatio("Backtrack1", this.backtrackCount[0], this.BacktrackCount);
		this.setRatio("Backtrack2", this.backtrackCount[1], this.BacktrackCount);
		this.setRatio("Backtrack4", this.backtrackCount[2], this.BacktrackCount);
		this.setRatio("Backtrack8", this.backtrackCount[3], this.BacktrackCount);
		this.setRatio1("Backtrack16", this.backtrackCount[4], this.BacktrackCount);
		this.setRatio1("Backtrack32", this.backtrackCount[5], this.BacktrackCount);
		this.setRatio1("Backtrack64", this.backtrackCount[6], this.BacktrackCount);
		this.setRatio1("Backtrack128", this.backtrackCount[7], this.BacktrackCount);
		this.setRatio1("Backtrack256", this.backtrackCount[8], this.BacktrackCount);
		this.setRatio1("Backtrack512", this.backtrackCount[9], this.BacktrackCount);
		this.setRatio1("Backtrack1024", this.backtrackCount[10], this.BacktrackCount);
		
		p.memoMap.stat(this);
		ckeckRepeatCounter();

		this.statObject(pego);
		p.peg.updateStat(this);
		this.setCount("ErapsedTime", this.ErapsedTime);  // ms
		this.setRatio("Latency", this.ConsumedLength, this.ErapsedTime);

		
		this.writeCSV();
	}
	
	private UMap<vData> csvMap = new UMap<vData>();
	
	private void set (vData data) {
		UStringBuilder sb = new UStringBuilder();
		data.stringfy(sb, false);
		System.out.println(sb.toString());
		this.csvMap.put(data.key, data);
	}
	
	private abstract class vData {
		String key;
		vData(String key) {
			this.key = key;
		}
		abstract void stringfy(UStringBuilder sb, boolean raw);
	}
	
	private class vText extends vData {
		String value;
		vText(String key, String value) {
			super(key);
			this.value = value;
		}
		@Override
		void stringfy(UStringBuilder sb, boolean raw) {
			if(!raw) {
				sb.append(this.key, ": ");
			}
			sb.append(this.value);
		}
	}

	private class vCount extends vData {
		long value;
		vCount(String key, long value) {
			super(key);
			this.value = value;
		}
		@Override
		void stringfy(UStringBuilder sb, boolean raw) {
			if(!raw) {
				sb.append(this.key, ": ");
			}
			sb.append(""+this.value);
		}
	}

	private class vRatio extends vData {
		double value;
		vRatio(String key, long v1, long v2) {
			super(key);
			this.value = (double)v1 / v2;
		}
		@Override
		void stringfy(UStringBuilder sb, boolean raw) {
			if(!raw) {
				sb.append(this.key, ": ");
				sb.append(String.format("%.4f", this.value));
			}
			else {
				sb.append("" + this.value);
			}
		}
	}


	public final void setText(String key, String v) {
		this.set(new vText(key, v));
	}

	public final void setCount(String key, long v) {
		this.set(new vCount(key, v));
	}

	public final void setCount1(String key, long v) {
		if(v > 0) {
			this.set(new vCount(key, v));
		}
	}

	public final void setRatio(String key, long v, long v2) {
		this.set(new vRatio(key, v, v2));
	}

	public final void setRatio1(String key, long v, long v2) {
		if(v > 0) {
			this.set(new vRatio(key, v, v2));
		}
	}

	public final void write(String text) {
		System.out.print(text);
	}
	
	private final void CSV(UStringBuilder sb, String key) {
		vData d = this.csvMap.get(key);
		if(d != null) {
			d.stringfy(sb, true);
		}
		else {
			sb.append(key);
		}
		sb.append(",");
	}

	public final void writeCSV() {
		UStringBuilder sb = new UStringBuilder();
		this.CSV(sb, "Parser");
		
		this.CSV(sb, "FileName");
		this.CSV(sb, "FileSize");
		this.CSV(sb, "ErapsedTime");
		this.CSV(sb, "Latency");
		this.CSV(sb, "ConsumedLength");
		this.CSV(sb, "BacktrackLength");
		this.CSV(sb, "Backtrack/Consumed");
		this.CSV(sb, "Backtrack");
		this.CSV(sb, "WorstBacktrack");
		this.CSV(sb, "BacktrackAverage");
		this.CSV(sb, "Backtrack1");
		this.CSV(sb, "Backtrack2");
		this.CSV(sb, "Backtrack4");
		this.CSV(sb, "Backtrack8");
		this.CSV(sb, "Backtrack16");
		this.CSV(sb, "Backtrack32");
		this.CSV(sb, "Backtrack64");
		this.CSV(sb, "Backtrack128");
		this.CSV(sb, "MemoHit");
		this.CSV(sb, "MemoMiss");
		this.CSV(sb, "Hit/Miss");
		
		this.CSV(sb, "UsedObject");
		this.CSV(sb, "DisposedObject");
		this.CSV(sb, "Disposal/Used");
		this.CSV(sb, "NewObject");
		this.CSV(sb, "New/Creation");
		this.CSV(sb, "ObjectEdge");
		this.CSV(sb, "ObjectNode");
		this.CSV(sb, "ObjectDepth");

		System.out.println(sb.toString());
	}
	
	
}