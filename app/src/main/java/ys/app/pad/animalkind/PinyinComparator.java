package ys.app.pad.animalkind;


import java.util.Comparator;

import ys.app.pad.model.AnimalTypeClassifyInfo;
import ys.app.pad.model.ServiceInfo;


/**
 *根据首字母比较排序
 *
 */
public class PinyinComparator implements Comparator<Object> {

	public int compare(Object lhs, Object rhs) {
		String ll="";
		String rr="";
		if (lhs instanceof AnimalTypeClassifyInfo && rhs instanceof AnimalTypeClassifyInfo){
			ll=((AnimalTypeClassifyInfo) lhs).getSortLetters();
			rr=((AnimalTypeClassifyInfo) rhs).getSortLetters();
		}else if(lhs instanceof ServiceInfo && rhs instanceof ServiceInfo){
			ll=((ServiceInfo) lhs).getSortLetters();
			rr=((ServiceInfo) rhs).getSortLetters();
		}
		if (ll.equals("@")
				|| rr.equals("#")) {
			return -1;
		} else if (ll.equals("#")
				|| rr.equals("@")) {
			return 1;
		} else {
			return ll.compareTo(rr);
		}
	}

}
