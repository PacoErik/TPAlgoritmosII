package tortoise;

@Table(test="ALTATABLA")
public class DEPARTMENT {
	@Id()
	@Column()
	int DEPT_ID;
	@Column()
	String DEPT_NAME;
	@Column()
	String DEPT_NO;
	@Column()
	String LOCATION;
	public int getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(int dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}
	public String getDEPT_NAME() {
		return DEPT_NAME;
	}
	public void setDEPT_NAME(String dEPT_NAME) {
		DEPT_NAME = dEPT_NAME;
	}
	public String getDEPT_NO() {
		return DEPT_NO;
	}
	public void setDEPT_NO(String dEPT_NO) {
		DEPT_NO = dEPT_NO;
	}
	public String getLOCATION() {
		return LOCATION;
	}
	public void setLOCATION(String lOCATION) {
		LOCATION = lOCATION;
	}
	
}
