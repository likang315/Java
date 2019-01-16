package com.xzy.dao.imp;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.xzy.dao.Email;
@Component(value="gmail")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)

public class Gmail implements Email {

/*	 @PostConstruct
	    public void populateMovieCache() {
	        // populates the movie cache upon initialization...
	    }

	    @PreDestroy
	    public void clearMovieCache() {
	        // clears the movie cache upon destruction...
	    }
	*/
	public void send() {
		// TODO Auto-generated method stub
		System.out.println("Gmail...................");
	}

}
