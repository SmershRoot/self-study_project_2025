package smersh.project.service.test;

import org.springframework.stereotype.Service;

@Service("testService")
public class TestService {
    public String doSomething() {
        return "Something has been done...";
    }
}
