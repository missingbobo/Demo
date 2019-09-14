package com.example.calbirthdaylib;

public interface Event {
	int TYPE_BIRTHDAY = 1;
	int TYPE_FESTIVAL = 2;
	
	int getSortArg();
	int getEventType();


}
