package TimeManager.ui.activitycontrollers;

import TimeManager.model.Task;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.*;

public class ActivityController {

    /**
     * The model that Primefaces needs for the graphical representation of the tasks (type PieChartModel from primefaces import)
     */
    protected PieChartModel model;

    /**
     * dailyActivities: stores the activities of the selectedDate
     * weeklyActivities: stores all activities of that week of that week
     * monthlyActivities: stores all activities of that user of that month
     */
    protected List<Task> dailyActivities = new ArrayList<Task>();
    protected List<Task> weeklyActivities = new ArrayList<Task>();
    protected List<Task> monthlyActivities = new ArrayList<Task>();
    protected List<Task> currentActivities = new ArrayList<Task>();

    /**
     * currentActivitiesPercentage: stores the activities of the currentActivities as well as the percentage
     */
    protected HashMap<String, Float> currentActivitiesPercentage = new HashMap<>();

    /**
     * @param selectedDate the Calendar Object that stores the Date entered by the user
     * @return returns the Monday of the respective week in which a day was selected by the user
     */
    @PreAuthorize("isAuthenticated()")
    protected Calendar determineWeekStart(Calendar selectedDate){

        // lower the selected day by one as long as monday of that week is reached (monday is 2)
        while(selectedDate.get(Calendar.DAY_OF_WEEK) != 2){
            selectedDate.add(Calendar.DAY_OF_MONTH, -1);
        }
        return selectedDate;
    }

    /**
     * @param selectedDate the Calendar Object that stores the Date entered by the user
     * @return returns the Sunday of the respective week in which a day was selected by the user
     */
    @PreAuthorize("isAuthenticated()")
    protected Calendar determineWeekEnd(Calendar selectedDate){

        // lower the selected day by one as long as monday of that week is reached
        while(selectedDate.get(Calendar.DAY_OF_WEEK) != 1){
            selectedDate.add(Calendar.DAY_OF_MONTH, +1);
        }
        return selectedDate;
    }

    /**
     * @param currentActivities includes the activities that should be displayed to the user
     * sets the activities of the currentActitvities as first parameter of the hashmap currentActivitiesPercentage
     * calculates the total time of all those activities
     */
    @PreAuthorize("isAuthenticated()")
    protected void generateHashMapPercent(List<Task> currentActivities){
        currentActivitiesPercentage.clear();    // reset the hashmap each time the method is executed
        float difference;
        long totalTime = 0;

        // calculates the total time of all activities in the list in minutes
        for (Task task : currentActivities) {
            totalTime += (task.getTaskEnd().getTime() - task.getTaskStart().getTime()) / 1000;
        }

        // adds every task category as a key to the hashmap and sums up the time for each task of that category as a value
        for (Task task : currentActivities) {
            difference = (task.getTaskEnd().getTime() - task.getTaskStart().getTime()) / 1000;
            String categoryName = task.getTaskCategory().toString();

            // to remove the brackets from the name
            String regx = "[]";
            char[] ca = regx.toCharArray();
            for (char c : ca) {
                categoryName = categoryName.replace(""+c, "");
            }

            if (currentActivitiesPercentage.containsKey(categoryName)) {
                difference += currentActivitiesPercentage.get(categoryName);
            }
            currentActivitiesPercentage.put(categoryName, difference);
        }

        // converts the time of each category in the hashmap to a percent-value
        for (String task : currentActivitiesPercentage.keySet()) {
            difference = (currentActivitiesPercentage.get(task) / totalTime * 100);
            double differenceRound = Math.round(difference * 100.0) / 100.0;    // round the difference to max two decimal points
            currentActivitiesPercentage.put(task, (float)differenceRound);    // convert the value of each category from the time to the percentage
        }
    }

    /**
     * fills the model with activities and percentage values
     * the information is saved from the corresponding HashMap activitiesPercentage
     */
    @PreAuthorize("isAuthenticated()")
    protected void fillChartWithData(){
        HashMap<String, Float> activitiesPercentage;
        activitiesPercentage = getCurrentActivitiesPercentage();

        Iterator it = activitiesPercentage.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            model.set(pair.getKey().toString(), (Number) pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    /**
     * Initializes the PieChart model for the graphical evaluation of the activity data. The fillChartWithData method is
     * called after initialization. It is determined that the model should indicate the percentages of the individual
     * activities and that a legend should be enjoyed.
     * @return the method returns a finished model that is already ready for output
     */
    @PreAuthorize("isAuthenticated()")
    public PieChartModel getModel() {
        model = new PieChartModel();
        fillChartWithData();
        model.setShowDataLabels(true);      // for showing the percentage-Numbers on the chart
        model.setLegendPosition("w");       // for showing a legend next to the chart as in the concept description
        return model;
    }

    /**
     *  This method clears the List of Activities whenever a new date was selected.
     *  Prevents incorrect activity data from being generated.
     */
    @PreAuthorize("isAuthenticated()")
    protected void clearActivityLists() {
        dailyActivities.clear();
        weeklyActivities.clear();
        monthlyActivities.clear();
        currentActivities.clear();
    }

    /**
     * This method checks whether an activity took place this month. If this is the case, true is returned, otherwise false.
     * @param activityYear the selected year
     * @param activityMonth the selected month
     * @return true if activity took place this month
     */
    @PreAuthorize("isAuthenticated()")
    protected Boolean notInThisMonth(int activityYear, int activityMonth){
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.add(Calendar.MONTH, -1);    // subtract a month from today
        return (today.get(Calendar.YEAR) >= activityYear && today.get(Calendar.MONTH) >= activityMonth);
    }

    /**
     * Depending on the selection of a user (stored in viewSelection), the generateMapPercent method is called and the
     * activity data is generated for either the daily, weekly or monthly view.
     * @param viewSelection provides information about which date was selected
     */
    @PreAuthorize("isAuthenticated()")
    protected void showViewSelection(int viewSelection){
        // switch case to determine if daily, weekly or monthly activities will be shown
        switch (viewSelection){
            case 0:
                generateHashMapPercent(dailyActivities);
                break;
            case 1:
                generateHashMapPercent(weeklyActivities);
                break;
            case 2:
                generateHashMapPercent(monthlyActivities);
                break;
        }
    }

    /**
     * this getter is called by method fillChartWithData
     * @return a HashMap with activities as key and and time it took in percent as a value
     */
    @PreAuthorize("isAuthenticated()")
    public HashMap<String, Float> getCurrentActivitiesPercentage() {
        return currentActivitiesPercentage;
    }

}
