package com.fx.scanapp.fileAnalyze;

public class FileAnalyze {
	public static boolean ReadINI(){
		try {
			String result=FileIO.readSDcard();
			if(result!=null){
				if(!result.equalsIgnoreCase("0"))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				FileIO.writeSDcard("1");
				return false;
			}
		} catch (SDException e) {
			e.printStackTrace();
		}
		return true;
		
	}
	
	public static void WriteINI(String str){
		try {
			FileIO.writeSDcard(str);
		} catch (SDException e) {
			e.printStackTrace();
		}
		
	}

}
