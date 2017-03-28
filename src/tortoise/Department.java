package tortoise;

@Table(name="DEPARTMENT")
public class Department {
	//@Id()
	@Column(name="DEPT_ID")
	int deptId;
	@Column(name="DEPT_NAME")
	String deptName;
	@Column(name="DEPT_NO")
	String deptNo;
	@Column(name="LOCATION")
	String location;
	

	
}
