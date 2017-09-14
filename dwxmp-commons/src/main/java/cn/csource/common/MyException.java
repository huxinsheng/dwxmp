/*
* Copyright (C) 2008 Happy Fish / HuXinsheng
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
*/

package cn.csource.common;

/**
* My Exception
* @author Happy Fish / HuXinsheng
* @version Version 1.0
*/
@SuppressWarnings("serial")
public class MyException extends Exception
{
    public MyException()
    {
    }
    
    public MyException(String message)
    {
    		super(message);
    }
}
