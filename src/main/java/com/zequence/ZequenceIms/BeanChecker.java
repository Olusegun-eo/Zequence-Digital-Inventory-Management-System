package com.zequence.ZequenceIms;


import com.zequence.ZequenceIms.service.token.ConfirmationTokenRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanChecker {

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void checkBeans() {
        String[] beanNames = applicationContext.getBeanNamesForType(ConfirmationTokenRepository.class);
        for (String beanName : beanNames) {
            System.out.println("Found bean: " + beanName);
        }
    }

    // Other methods for checking other beans in the repository
}
