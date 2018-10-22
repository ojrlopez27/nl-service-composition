package edu.cmu.inmind.services.apis;

import edu.cmu.inmind.osgi.commons.markers.BundleAPI;
import edu.cmu.inmind.osgi.commons.markers.Feature;
import edu.cmu.inmind.services.markers.ArgDesc;
import edu.cmu.inmind.services.markers.Description;
import java.util.List;

import static edu.cmu.inmind.services.apis.ListCalculatorService.SERVICE;

@BundleAPI(id = SERVICE)
public interface ListCalculatorService extends CalculatorService {

    /** this is the service id **/
    String SERVICE = "ListCalculatorService";

    @Description(capabilities = {
            "This method adds a list of numbers",
            "This method gives the sum of a list of numbers"
    })
    @ArgDesc(args = {
            "numbers : what are the numbers?"
    })
    @Feature(id = CALCULATOR_SUM,
            description = "This method returns the sum of a list of numbers",
            keywords = {CALCULATOR_SUM, "add", "summation", "plus"})
    void add(List<Number> numbers);

    @Description(capabilities = {
            "This method multiplies a list of numbers",
            "This method gives the product of a list of numbers"
    })
    @ArgDesc(args = {
            "numbers : what are the numbers?"
    })
    @Feature(id = CALCULATOR_MULTIPLY,
            description = "This method returns the product of a list of numbers",
            keywords = {CALCULATOR_MULTIPLY, "times", "multiplication", "multiply"})
    void multiply(List<Number> numbers);
}