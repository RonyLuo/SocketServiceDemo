package com.example.socketdemo;

/**
 * @since 2016/3/14
 * @author larsonzhong@163.com
 *
 */
public interface IService {
	/**
	 * course the data type must be string for security
	 * @param dataStr
	 */
    public boolean sendData(String dataStr);
}
