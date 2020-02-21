package xmlvalidator;

import static java.lang.System.*;
import static sbcc.Core.*;

public class BasicXmlTagStack implements XmlTagStack {

	private static final int INITIAL_SIZE = 10;

	private XmlTag[] arr;
	private int top = -1;

	public BasicXmlTagStack() {
		super();
		arr = new XmlTag[INITIAL_SIZE];
	}


	@Override
	public void push(XmlTag item) {
		if (top == arr.length - 1) {
			XmlTag[] temp = new XmlTag[(int) (arr.length * 1.25)];
			arraycopy(arr, 0, temp, 0, arr.length);
			arr = temp;
		}
		arr[++top] = item;
	}


	@Override
	public XmlTag pop() {
		if (top > -1) {
			var item = arr[top];
			arr[top] = null;
			top--;
			return item;
		}
		return null;
	}


	@Override
	public XmlTag peek(int position) {
		if ((position >= 0) && ((top - position) > -1)) {
			return arr[top - position];
		}
		return null;
	}


	@Override
	public int getCount() {
		return top + 1;
	}

}
