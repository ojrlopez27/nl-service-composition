package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.osgi.commons.markers.Feature;
import edu.cmu.inmind.services.commons.GenericService;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;

import static edu.cmu.inmind.services.apis.CalculatorService.SERVICE;

@BundleAPI(id = SERVICE)
public interface CalculatorService extends GenericService {

    /** this is the service id **/
    String SERVICE = "CalculatorService";

    /** Constants we will need when extracting features from GenericPOJO **/
    String CALCULATOR_SUM           = "add";
    String CALCULATOR_MULTIPLY      = "multiply";

    /** Let's define some aliases for Bundle implementations **/
    String CALCULATOR_INTEGER   = "calculator-integer";
    String CALCULATOR_LONG      = "calculator-long";
    String CALCULATOR_CRAZY     = "calculator-crazy";

    @Description(capabilities = {
            "This method adds two numbers",
            "This method computes the sum of two numbers"
    })
    @ArgDesc(args = {
            "one : what is the first number?",
            "two : what is the other number?"
    })
    @Feature(id = CALCULATOR_SUM,
            description = "This method returns the sum of two numbers",
            keywords = {"add", "summation", "plus"})
    void add(Number one, Number two);

    @Description(capabilities = {
            "This method multiplies two numbers",
            "This method gives the product of two numbers"
    })
    @ArgDesc(args = {
            "one : what is the first number?",
            "two : what is the other number?"
    })
    @Feature(id = CALCULATOR_MULTIPLY,
            description = "This method returns the multiplication of two numbers",
            keywords = {"times", "multiplication", "multiply"})
    void multiply(Number one, Number two);

}
