package entity;

public class Todos {
	private int todo_id;
	private String content;
	private String memo;
	private String duedate;
	private String duetime;
	private String share_with;
	private String importance;
	
	private int writer_id;
	private int addr_id;
	
	private boolean done;
	
	public Todos() {
		super();
	}

	public int getTodo_id() {
		return todo_id;
	}

	public void setTodo_id(int todo_id) {
		this.todo_id = todo_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDuedate() {
		return duedate;
	}

	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}

	public String getDuetime() {
		return duetime;
	}

	public void setDuetime(String duetime) {
		this.duetime = duetime;
	}

	public String getShare_with() {
		return share_with;
	}

	public void setShare_with(String share_with) {
		this.share_with = share_with;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public int getWriter_id() {
		return writer_id;
	}

	public void setWriter_id(int writer_id) {
		this.writer_id = writer_id;
	}

	public int getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(int addr_id) {
		this.addr_id = addr_id;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	@Override
	public String toString() {
		return "Todos [todo_id=" + todo_id + ", content=" + content + ", memo=" + memo + ", duedate=" + duedate
				+ ", duetime=" + duetime + ", share_with=" + share_with + ", importance=" + importance + ", writer_id="
				+ writer_id + ", addr_id=" + addr_id + ", done=" + done + "]";
	}
	
	
	
	
}
