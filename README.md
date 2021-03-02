# jltest

This uses the Kotlin Ktor web framework.

To run locally, run main and use:
 
```curl --location --request GET 'http://localhost:8081/products?labelType=ShowWasThenNow'```

The url and key of the webservice used as the source of the data are in 
```resources/application.conf```.

### Issues:
 
#### Was and Now can contain either empty string, a string or {from:, to:}.

The label generator will create a "Now £x.xx" if the __was__ is empty but "Was £0.00, now £x.xx" if the __was__ is set to zero.

If any price contains a from/to value then the label generator shows both value e.g. "from £1.50 - 2.50".

#### Problems with prices.

I couldn't find any examples but prices are represented in the source as strings so could contain invalid or negative numbers.
A tolerent reader approach has been taken. In most cases whatever is present is echoed but only valid prices are formatted. 

e.g. if the original now price is "abc" then the price returned is also "abc".
e.g. if the original now price is "-10.00" then the price returned is also "-10.00" rather than "-10".

If the showPercDisc parameter is passed an there is an invalid price then an error is shown on the label.

#### Basic colors can contain almost any value.

A list of 140 standard html colors are used in the look up map and "unknown" is returned if a color isn't found.

