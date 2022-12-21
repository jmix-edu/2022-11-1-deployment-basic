package com.company.tm.app;

import com.company.tm.entity.Project;
import com.company.tm.entity.Task;
import io.jmix.core.DataManager;
import io.jmix.core.EntitySet;
import io.jmix.core.SaveContext;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Profile("!dev")
//@Profile("default")
@Component("tm_TaskImportService")
public class TaskImportServiceImpl implements TaskImportService {

    private static final Logger log = LoggerFactory.getLogger(TaskImportServiceImpl.class);

    @Autowired
    private DataManager dataManager;

    @Override
    public int importTasks() {
        List<String> externalTaskNames = obtainExternalTaskNames();
        Project defaultProject = loadDefaultProject();

        List<Task> tasks = externalTaskNames.stream()
                .map(name -> {
                    Task task = dataManager.create(Task.class);
                    task.setName(name);
                    task.setProject(defaultProject);

                    return task;
                })
                .collect(Collectors.toList());

        EntitySet entitySet = dataManager.save(new SaveContext().saving(tasks));
        log.info("{} tasks imported", entitySet.size());
        return entitySet.size();
    }

    private List<String> obtainExternalTaskNames() {
        return Stream.iterate(0, i -> i).limit(5)
                .map(i -> "Task " + RandomStringUtils.randomAlphabetic(5))
                .collect(Collectors.toList());
    }

    @Nullable
    private Project loadDefaultProject() {
        Optional<Project> entity = dataManager.load(Project.class)
                .query("select p from tm_Project p where p.defaultProject = :defaultProject")
                .parameter("defaultProject", true)
                .optional();

        return entity.orElse(null);
    }
}