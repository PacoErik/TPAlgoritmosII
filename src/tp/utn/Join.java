package tp.utn;

public class Join
{
	private String s;
	private String field;

	public String getS()
	{
		if(s==null) return "";
		return s;
	}

	public void setS(String s)
	{
		this.s=s;
	}

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field=field;
	}
}
