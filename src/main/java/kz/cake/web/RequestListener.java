package kz.cake.web;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class RequestListener implements ServletRequestListener {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public void requestInitialized(ServletRequestEvent sre) {

    }
}
