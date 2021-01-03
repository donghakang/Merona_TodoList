package entity;

public class Todos {
	private String content;
	private boolean isDone;
	
	public Todos() {
		super();
	}
	public Todos(String content, boolean isDone) {
		super();
		this.content = content;
		this.isDone = isDone;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isDone() {
		return isDone;
	}
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	
	@Override
	public String toString() {
		return "Todos [content=" + content + ", isDone=" + isDone + "]";
	}

	
}
