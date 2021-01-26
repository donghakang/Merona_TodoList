package entity;

public class AddressTodo {
	Todos todos;
	Address address;
	boolean notify;
	
	
	public AddressTodo() {
		super();
	}

	public Todos getTodos() {
		return todos;
	}

	public void setTodo(Todos todos) {
		this.todos = todos;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public boolean isNotify() {
        return notify;
    }

    public void setNotified(boolean notify) {
        this.notify = notify;
    }


	@Override
	public String toString() {
		return "AddressTodo [todo=" + todos.toString() + ", address=" + address.toString() + "]";
	}
	
	
}	
