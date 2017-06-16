package org.reapsn.paceditor.util;

import sun.org.mozilla.javascript.internal.NativeArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reaps on 2017/6/16.
 */
public class JavaScriptUtil {

	public static <T> List<T> convertToJavaList(NativeArray nativeArray, Class<T> claz) {

		if (nativeArray == null) {
			return null;
		}

		boolean contentObjIsArray = false;
		List<T> list = new ArrayList<T>();

		Class contentType = getContentType(nativeArray);

		if (contentType == null) {
			return list;
		}

		if (claz.isAssignableFrom(contentType)) {

		} else if (NativeArray.class.isAssignableFrom(contentType) && java.util.List.class.getName().equals(claz.getName())) {
			contentObjIsArray = true;
		} else {
			throw new IllegalArgumentException("can't convert ");
		}

		Object[] ids = nativeArray.getIds();

		for (Object id :
				ids) {

			int index = (Integer) id;
			Object obj = nativeArray.get(index, null);

			if (contentObjIsArray) {
				Class tempContentType = getContentType((NativeArray) obj);
				if (tempContentType != null && NativeArray.class.isAssignableFrom(tempContentType)) {
					list.add((T) convertToJavaList((NativeArray) obj, claz));
				} else {
					list.add((T) convertToJavaList((NativeArray) obj, Object.class));
				}
			} else {
				list.add((T) obj);
			}
		}

		return list;
	}

	public static Class getContentType(NativeArray nativeArray) {
		if (nativeArray == null) {
			return null;
		}
		if (nativeArray.getLength() > 0L) {
			Object obj = nativeArray.get(0, null);
			return obj.getClass();
		} else {
			return null;
		}
	}
}
