/*
 * Created 18.01.2009
 *
 * (c) 2009 Thorsten M�ller - University of Basel Switzerland
 *
 * The MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package examples;

import java.net.URI;

/**
 * Utility interface that contains often used URIs of exemplary OWL
 * ontologies as well as OWL-S services.
 *
 * @author unascribed
 * @version $Rev:$; $Author:$; $Date:$
 */
public interface ExampleURIs
{
	public static final String BASE_URI = "http://dangiankit.ddns.net/~dangiankit/";
	
	/* ---------------------------------------------------------------- */
	/* OWL-S Services                                                   */
	/* ---------------------------------------------------------------- */
	@Deprecated
	public static final URI AMAZON_BOOK_PRICE_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/AmazonBookPrice.owl");

	public static final URI AMAZON_BOOK_PRICE_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/AmazonBookPrice.owl");

	@Deprecated
	public static final URI ANYORDER_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/AnyOrder.owl");

	public static final URI ANYORDER_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/AnyOrder.owl");

	@Deprecated
	public static final URI BABELFISH_TRANSLATOR_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/BabelFishTranslator.owl");

	public static final URI BABELFISH_TRANSLATOR_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/BabelFishTranslator.owl");

	@Deprecated
	public static final URI BN_BOOK_PRICE_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/BNPrice.owl");

	public static final URI BN_BOOK_PRICE_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/BNPrice.owl");

	@Deprecated
	public static final URI BOOK_FINDER_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/BookFinder.owl");

	public static final URI BOOK_FINDER_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/BookFinder.owl");

	@Deprecated
	public static final URI BOOK_PRICE_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/BookPrice.owl");

	public static final URI BOOK_PRICE_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/BookPrice.owl");

	@Deprecated
	public static final URI BRAVO_AIR_SERVICE_OWLS11 =
		URI.create("http://www.daml.org/services/owl-s/1.1/BravoAirService.owl");
// http://www.daml.org/services/owl-s/1.1/BravoAirService.owl imports time-entry.owl which was relocated

	public static final URI BRAVO_AIR_SERVICE_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/BravoAirService.owl");

	@Deprecated
	public static final URI BRAVO_AIR_PROFILE_OWLS11 =
		URI.create("http://www.daml.org/services/owl-s/1.1/BravoAirProfile.owl");

	public static final URI BRAVO_AIR_PROFILE_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/BravoAirProfile.owl");

	@Deprecated
	public static final URI BRAVO_AIR_PROCESS_OWLS11 =
		URI.create("http://www.daml.org/services/owl-s/1.1/BravoAirProcess.owl");

	public static final URI BRAVO_AIR_PROCESS_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/BravoAirProcess.owl");

	@Deprecated
	public static final URI BRAVO_AIR_GROUNDING_OWLS11 =
		URI.create("http://www.daml.org/services/owl-s/1.1/BravoAirGrounding.owl");

	public static final URI BRAVO_AIR_GROUNDING_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/BravoAirGrounding.owl");

	@Deprecated
	public static final URI CHOICE_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/Choice.owl");

	public static final URI CHOICE_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/Choice.owl");

	public static final URI CONGO_PROCESS =
		URI.create("http://www.daml.org/services/owl-s/1.2/CongoProcess.owl");

	@Deprecated
	public static final URI CURRENCY_CONVERTER_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/CurrencyConverter.owl");

	public static final URI CURRENCY_CONVERTER_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/CurrencyConverter.owl");

	@Deprecated
	public static final URI DICTIONARY_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/Dictionary.owl");
//	http://www.mindswap.org/2004/owl-s/1.1/Dictionary.owl

	public static final URI DICTIONARY_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/Dictionary.owl");

	public static final URI EXISTENTIAL_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/Existential.owl");

	@Deprecated
	public static final URI FIND_CHEAPER_BOOK_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/FindCheaperBook.owl");

	public static final URI FIND_CHEAPER_BOOK_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/FindCheaperBook.owl");

	@Deprecated
	public static final URI FIND_LAT_LONG_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/FindLatLong.owl");

	public static final URI FIND_LAT_LONG_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/FindLatLong.owl");

	@Deprecated
	public static final URI FOREACH_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/ForEach.owl");

	public static final URI FOREACH_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/ForEach.owl");

	@Deprecated
	public static final URI FRENCH_DICTIONARY_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/FrenchDictionary.owl");

	public static final URI FRENCH_DICTIONARY_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/FrenchDictionary.owl");

	@Deprecated
	public static final URI IF_THEN_ELSE1_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/IfThenElse-1.owl");

	public static final URI IF_THEN_ELSE1_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/IfThenElse-1.owl");

	@Deprecated
	public static final URI IF_THEN_ELSE2_OWLS11 =
		URI.create(BASE_URI + "owl-s/1.1/IfThenElse-2.owl");

	public static final URI IF_THEN_ELSE2_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/IfThenElse-2.owl");

	@Deprecated
	public static final URI ZIP_CODE_FINDER_OWLS11 =
		URI.create("http://www.mindswap.org/2004/owl-s/1.1/ZipCodeFinder.owl");

	public static final URI ZIP_CODE_FINDER_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/ZipCodeFinder.owl");

	@Deprecated
	public static final URI JGROUNDING_OWLS11 =
		URI.create("http://www.ifi.unizh.ch/ddis/ont/owl-s/JGroundingTest.owl");

	public static final URI JGROUNDING_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/JGroundingTest.owl");

	public static final URI PRODUCE_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/Produce.owl");

	public static final URI REPEAT_UNTIL_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/RepeatUntil.owl");

	public static final URI REPEAT_WHILE_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/RepeatWhile.owl");

	public static final URI SPLIT_JOIN_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/SplitJoin.owl");

	@Deprecated
	public static final URI SERVICE_EXTENSION_OWLS11 =
		URI.create("http://www.ifi.unizh.ch/ddis/ont/owl-s/ServiceExtensionTest.owl");

	public static final URI SERVICE_EXTENSION_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/ServiceExtensionTest.owl");

	public static final URI VALUEFUNCTION_OWLS12 =
		URI.create(BASE_URI + "owl-s/1.2/ValueFunction.owl");

	/* ---------------------------------------------------------------- */
	/* OWL Ontologies                                                   */
	/* ---------------------------------------------------------------- */
	public static final String ONT_AN_SERVICE = BASE_URI + "owl-s/1.1/ANService.owl#";

	public static final String ONT_CURRENCIES = BASE_URI + "owl/currency.owl#";

	public static final String ONT_FACTBOOK_LANGUAGES = BASE_URI + "owl/2003/09/factbook/languages.rdf#";

	public static final String ONT_LUBM = "http://www.lehigh.edu/%7Ezhp2/2004/0401/univ-bench.owl";

	public static final String ONT_MINDSWAP_CONCEPTS = BASE_URI + "owl/concepts.owl#";

	public static final String ONT_MINDSWAP_PROFILE =  BASE_URI + "owl-s/1.2/MindswapProfileHierarchy.owl#";

	public static final String ONT_PRICE = BASE_URI + "owl/price.owl#";

	public static final String ONT_WINE = "http://www.w3.org/2001/sw/WebOnt/guide-src/wine.owl#";

	public static final String ONT_ZIPCODE = "http://www.daml.org/2001/10/html/zipcode-ont#";


}
