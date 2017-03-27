package tortoise;

@Table(name="DEPARTMENT")
public class Department {
	@Id()
	@Column(name="DEPT_ID")
	int deptId;
	@Column(name="DEPT_NAME")
	String deptName;
	@Column(name="DEPT_NO")
	String deptNo;
	@Column(name="LOCATION")
	String location;
	
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	
}
