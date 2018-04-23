package com.company.client;

import com.company.user.User;
import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

/**Starting point of client program*/
public class Client {
	public static void main(String[] args) {
		new SignInInterface();
	}
}