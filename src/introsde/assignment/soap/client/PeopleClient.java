package introsde.assignment.soap.client;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import introsde.assignment.soap.ws.People;
import introsde.assignment.soap.ws.PeopleService;
import introsde.assignment.soap.ws.Person;
import introsde.assignment.soap.ws.HealthProfile;
import introsde.assignment.soap.ws.HealthProfileHistory;

public class PeopleClient {
	
	static PrintWriter logWriter;

    public static void main(String[] args) throws Exception {
    	// Define writer
    	logWriter = new PrintWriter("client.log", "UTF-8");

    	// Print server wsdl URL
    	String wsdl = "https://warm-brook-6204.herokuapp.com/ws/people?wsdl";
    	printAndLog("===============================================================");
    	printAndLog("Server deployed at: " + wsdl);

    	// New people service
    	PeopleService service = new PeopleService();
        People people = service.getPeopleImplPort();

        // METHOD #1
        printAndLog("\n---------------------------------------------------------------");
        printAndLog("Method #1: readPerson(56)");
        Person person = people.readPerson(new Long(56));
        printAndLog("Result ==> Person: " + personToString(person));
        
        // METHOD #2
        printAndLog("\n---------------------------------------------------------------");
        printAndLog("Method #2: readPersonList()");
        List<Person> pList = people.readPersonList();
        printAndLog("Result ==> Found " + pList.size() + " people:");
        for (Person p : pList) {
        	printAndLog("\t" + personToString(p));
        }
        
        // METHOD #3
        printAndLog("\n---------------------------------------------------------------");
        printAndLog("Method #3: updatePerson(person) [firstname: TEST NEW NAME]");
        person.setFirstname("TEST NEW NAME");
        Long personId = people.updatePerson(person);
        Person testPerson = people.readPerson(personId);
        printAndLog("Result ==> Person: " + personToString(testPerson));
        
        // METHOD #4
        printAndLog("\n---------------------------------------------------------------");
        printAndLog("Method #4: createPerson(newPerson) [firstname: Chuck]");
        
        // Create new person
        Person newPerson = new Person();
        newPerson.setFirstname("Chuck");
        newPerson.setLastname("Norris");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(sdf.parse("1960/01/01"));
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        newPerson.setBirthdate(date);
        newPerson.setEmail("chucknorris@gmail.com");
        
        // Add health profile
        HealthProfile measure = new HealthProfile();
        measure.setType("height");
        measure.setValue("175");
        measure.setValueType("cm");
        Person.HealthProfile hp = new Person.HealthProfile();
        List<HealthProfile> hpList = hp.getMeasure();
        hpList.add(measure);
        newPerson.setHealthProfile(hp);
        
        newPerson = people.createPerson(newPerson);
        printAndLog("Result ==> Person: " + personToString(newPerson));
        
        // METHOD #5
        printAndLog("\n---------------------------------------------------------------");
        personId = newPerson.getId();
        printAndLog("Method #5: deletePerson(" + personId + ")");
        people.deletePerson(personId);
        Person deletedPerson = people.readPerson(personId);
        printAndLog("Result ==> Person with id " + personId +": " + personToString(deletedPerson));
        
        // METHOD #6
        printAndLog("\n---------------------------------------------------------------");
        printAndLog("Method #6: readPersonHistory(" + person.getId() + ", 'weight')");
        List<HealthProfileHistory> hpHistory = people.readPersonHistory(person.getId(), "weight");
        printAndLog("Result ==> Found " + hpHistory.size() + " measures:");
        HealthProfileHistory measureExample = new HealthProfileHistory();
        for (HealthProfileHistory hMeasure : hpHistory) {
        	printAndLog("\t" + healthProfileHistoryToString(hMeasure));
        	// Save measure for method 8
        	measureExample = hMeasure;
        }
        
        // METHOD #7
        printAndLog("\n---------------------------------------------------------------");
        printAndLog("Method #7: readMeasureTypes()");
        List<String> measures = people.readMeasureTypes();
        printAndLog("Result ==> Found " + measures.size() + " measure types:");
        for (String m : measures) {
        	printAndLog("\t" + m);
        }
        
        // METHOD #8
        printAndLog("\n---------------------------------------------------------------");
        printAndLog("Method #8: readPersonMeasure(" + person.getId() + ", weight, " + measureExample.getMid() + ")");
        String value = people.readPersonMeasure(person.getId(), "weight", measureExample.getMid());
        printAndLog("Result ==> MeasureID = " + measureExample.getMid() + ", weight: " + value);
        
        // METHOD #9
        printAndLog("\n---------------------------------------------------------------");
        printAndLog("Method #9: savePersonMeasure(" + person.getId() + ", newMeasure) [height: 180 cm]");
        HealthProfile newMeasure = new HealthProfile();
        newMeasure.setType("height");
        newMeasure.setValue("180");
        newMeasure.setValueType("cm");
        c.setTime(new Date());
        date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        newMeasure.setCreated(date);
        
        HealthProfile returnedMeasure = people.savePersonMeasure(person.getId(), newMeasure);
        printAndLog("Result ==> MeasureID = " + returnedMeasure.getMid()
        	+ ", " + returnedMeasure.getType() + ": " + returnedMeasure.getValue());
        
        // METHOD #10
        printAndLog("\n---------------------------------------------------------------");
        printAndLog("Method #10: updatePersonMeasure(" + person.getId() + ", measure) [value: 100]");
        measureExample.setValue("100");
        Long measureId = people.updatePersonMeasure(person.getId(), measureExample);
        String measureValue = people.readPersonMeasure(person.getId(), "weight", measureId);
        printAndLog("Result ==> MeasureID = " + measureId + ", weight: " + measureValue);
        
        // Close writer
        logWriter.close();
    }
    
    /**
     * Helper method to stringify person
     * @param p
     * @return
     */
    private static String personToString(Person p) {
    	if (p == null) {
    		return "NULL";
    	}

    	return p.getId() + " " + p.getFirstname() + " " + p.getLastname();
    }
    
    /**
     * Helper method to stringify health profile history
     * @param measure
     * @return
     */
    private static String healthProfileHistoryToString(HealthProfileHistory measure) {
    	if (measure == null) {
    		return "NULL";
    	}
    	
    	return measure.getType() + ": " + measure.getValue() + " " + measure.getValueType();
    }
    
    /**
     * Helper method to print to console and write to log
     * @param message
     */
    private static void printAndLog(String message) {
    	System.out.println(message);
    	logWriter.println(message);
    }
    
}