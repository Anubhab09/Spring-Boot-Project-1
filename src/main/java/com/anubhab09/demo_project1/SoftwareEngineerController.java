package com.anubhab09.demo_project1;

// Simply just to test an api
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/Software-Engineers")
public class SoftwareEngineerController {
    @GetMapping
    public List<SoftwareEngineer> getSoftwareEngineers(){
        return List.of(
                new SoftwareEngineer(
                        1,
                        "John",
                        "Java, MongoDB, AWS"
                ),
                new SoftwareEngineer(
                        2,
                        "Cena",
                        "React, Next JS, TypeScript"
                )
        );

    }


}
