package com.iamyasas.springbootjwtauthdemo.models;

public class Employee {
	private int id;
	private String name;
	private String age;
	private String cegName;
	private String departmentName;
	
	public Employee() {
		super();
	}

	public Employee(int id, String name, String age, String cegName, String departmentName) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.cegName = cegName;
		this.departmentName = departmentName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCegName() {
		return cegName;
	}

	public void setCegName(String cegName) {
		this.cegName = cegName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

}
