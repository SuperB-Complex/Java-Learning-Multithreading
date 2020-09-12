package tta.basics.dayseven.one;

import java.lang.reflect.Field;

public class CopyUtil {
	
	public static void copy(Object obj1, Object obj2) throws TypeException {
		Class class1 = obj1.getClass(), class2 = obj2.getClass();
		if (!checkType(class1, class2)) {
			throw new TypeException();
		}
		
		Field[] feilds1 = class1.getFields(), feilds2 = class2.getFields();
		for (Field f : feilds1) {
			System.out.println(f);
		}
	}
	
	private static boolean checkType(Class class1, Class class2) {
		if (class1 == class2) {
			return true;
		}
		return false;
	}
	
	private static void setPublicVariable(Field feild1, Field feild2) {
		
	}
}
