package TimeManager.rest;
import TimeManager.model.Task;
import TimeManager.model.TaskCategory;
import TimeManager.model.TimeFlip;
import TimeManager.model.User;
import TimeManager.repositories.UserRepository;
import TimeManager.services.MailService;
import TimeManager.services.TaskService;
import TimeManager.services.TimeFlipService;
import ch.qos.logback.core.CoreConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The Service part of our RestService.
 * Here most of the logistic happens.
 */
@Service
public class RestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeFlipService timeFlipService;

    @Autowired
    private MailService mailService = new MailService();

    @Autowired
    private TaskService taskService = new TaskService();

    private Integer side0 = 1;
    private Integer side1 = 2;
    private Integer side2 =10;
    private Integer side3 =12;
    private Integer side4 =9;
    private Integer side5 =11;
    private Integer side6 = 4;
    private Integer side7 = 8;
    private Integer side8 =7;
    private Integer side9 =17;
    private Integer side10 =13;
    private Integer side11 =14;

    private Integer counter = 1200;

    private static final AtomicLong id_count = new AtomicLong(1);

    private static final ConcurrentLinkedQueue<RestObject> queue = new ConcurrentLinkedQueue<>();

    /**
     * @return Id which counts up to get unique numbers.
     */
    static Long getNextId() {
        return id_count.getAndIncrement();
    }

    /**
     * Here we get the RestObject from the Pi and save it in the queue.
     * @param timeflipMac The MAC address of the timeflip.
     * @param content the content of the timeflip.
     * @return returns the finished RestObject.
     */
    public RestObject getRestObject(String timeflipMac, String content) {

        RestObject restObject = new RestObject();
        restObject.setTimeflipMac(timeflipMac);
        restObject.setContent(content);
        restObject.setID(getNextId());

        queue.add(restObject);

        return restObject;
    }

    /**
     * This class writes the data in the Database or write an E-Mail for the Warning.
     * It will do that every 24 Hours.
     */
    @Scheduled(cron="0 10 0 * * *")
    public void writeTasks(){
        while(queue.isEmpty() == false){
            RestObject restObject = queue.poll();
            if(restObject.getContent().equals("Connection") || restObject.getContent().equals("Battery")){
                checkWarning(restObject);
            }else{
                getData(restObject);
            }
        }
    }

    /**
     * @return returns the whole queue of our restobjects.
     */
    public List<RestObject> getRestObjects() {
        return new ArrayList<>(queue);
    }


    /**
     * If it is a Warning, we will send an appropriate E-Mail.
     * @param restObject we need the Object for the MacAddress and of course the content.
     */
    public void checkWarning(RestObject restObject){
        TimeFlip timeFlip = timeFlipService.findTimeFlipByMACAddress(restObject.getTimeflipMac());
        User user = userRepository.findUserByTimeFlip(timeFlip);

        if (restObject.getContent().equals("Connection")) {
            mailService.sendMail(user, "Warnung: Verbindungsproblem TimeFlip", "Liebe/r Benutzer/in!\n\n Bei ihrem Würfel #" + restObject.getTimeflipMac() + " liegt ein Problem vor.\n" + "Es konnte keine Verbindung zum Würfel hergestellt werden. Sollte dies noch einmal vorkommen, wenden Sie sich an den Administrator.\n\nMit freundlichen Grüßen,\n Ihr TimeManager Team");
        } else if (restObject.getContent().equals("Battery")) {
            mailService.sendMail(user, "Warnung: Niedriger Batteriestand", "Liebe/r Benutzer/in!\n\n Bei ihrem Würfel #" + restObject.getTimeflipMac() + "liegt ein Problem vor.\n" + "Ihr Batteriestand beträgt nur mehr 20 Prozent. Bitte tauschen Sie ehestmöglich die Batterie aus.\n\nMit freundlichen Grüßen,\n Ihr TimeManager Team");
        }
    }

    /**
     * If it is Data that we got we need to save it in the database.
     * @param restObject For that we need of course the MAC address and the content we want to save.
     */
    public void getData(RestObject restObject){

        TimeFlip timeFlip = timeFlipService.findTimeFlipByMACAddress(restObject.getTimeflipMac());
        User user = userRepository.findUserByTimeFlip(timeFlip);


        Map<Integer,Integer> cubeSides = new HashMap<Integer, Integer>();
        cubeSides.put(side0,0);
        cubeSides.put(side1,1);
        cubeSides.put(side2,2);
        cubeSides.put(side3,3);
        cubeSides.put(side4,4);
        cubeSides.put(side5,5);
        cubeSides.put(side6,6);
        cubeSides.put(side7,7);
        cubeSides.put(side8,8);
        cubeSides.put(side9,9);
        cubeSides.put(side10,10);
        cubeSides.put(side11,11);

        Map<Integer,String> timeFlipConfiguration = timeFlip.getTimeFlipConfiguration();

        counter++;

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        String content = restObject.getContent();

        String stringside = content.substring(0, content.indexOf(","));
        content = content.substring(stringside.length() + 1);
        String stringstart = content.substring(0, content.indexOf(","));
        content = content.substring(stringstart.length() + 1);
        String stringend = content;


        int side = 1;
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            side = Integer.parseInt(stringside);
            start.setTime(sdf.parse(stringstart));
            end.setTime(sdf.parse(stringend));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Task task = new Task();
        task.setCreateUser(user);
        task.setTaskId(counter);
        Set<TaskCategory> tempSet = new HashSet<TaskCategory>();
        String tempside = timeFlipConfiguration.get(cubeSides.get(side));
        System.out.println(tempside);
        if(tempside.equals(TaskCategory.BREAK.toString())){tempSet.add(TaskCategory.BREAK);}
        if(tempside.equals(TaskCategory.CONCEPTION.toString())){tempSet.add(TaskCategory.CONCEPTION);}
        if(tempside.equals(TaskCategory.DESIGN.toString())){tempSet.add(TaskCategory.DESIGN);}
        if(tempside.equals(TaskCategory.IMPLEMENTATION.toString())){tempSet.add(TaskCategory.IMPLEMENTATION);}
        if(tempside.equals(TaskCategory.TESTING.toString())){tempSet.add(TaskCategory.TESTING);}
        if(tempside.equals(TaskCategory.DOCUMENTATION.toString())){tempSet.add(TaskCategory.DOCUMENTATION);}
        if(tempside.equals(TaskCategory.ERROR_ANALYSIS.toString())){tempSet.add(TaskCategory.ERROR_ANALYSIS);}
        if(tempside.equals(TaskCategory.MEETING.toString())){tempSet.add(TaskCategory.MEETING);}
        if(tempside.equals(TaskCategory.CUSTOMER_MEETING.toString())){tempSet.add(TaskCategory.CUSTOMER_MEETING);}
        if(tempside.equals(TaskCategory.TRAINING.toString())){tempSet.add(TaskCategory.TRAINING);}
        if(tempside.equals(TaskCategory.PROJECT_MANAGEMENT.toString())){tempSet.add(TaskCategory.PROJECT_MANAGEMENT);}
        if(tempside.equals(TaskCategory.OTHERS.toString())){tempSet.add(TaskCategory.OTHERS);}

        task.setTaskCategory(tempSet);
        task.setTaskEnd(end.getTime());
        task.setTaskStart(start.getTime());

        taskService.writeTask(task, user);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
