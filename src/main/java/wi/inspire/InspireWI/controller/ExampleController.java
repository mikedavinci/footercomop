package wi.inspire.InspireWI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wi.inspire.InspireWI.model.Example;
import wi.inspire.InspireWI.repository.ExampleRepository;

import java.util.List;

@RestController
@RequestMapping("/example")

public class ExampleController {
    private final ExampleRepository exampleRepository;

    public ExampleController(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    @GetMapping("/all")
    public List<Example> getAllExamples() {
        List<Example> examples = exampleRepository.findAll();
        if (examples.isEmpty()) {
            Example example = new Example();
            example.setEmail("frank@example.com");
            example.setName("Frank");
            exampleRepository.save(example);
            examples = exampleRepository.findAll();
        }

        return examples;

    }

}