package entity;

public class Todos {
	private int todo_id;
	private String content;
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
	public boolean getDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	
	
	@Override
	public String toString() {
		return "Todos [todo_id=" + todo_id + ", content=" + content + ", done=" + done + "]";
	}
	
	
	
}
