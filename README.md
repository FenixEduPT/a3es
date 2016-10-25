# a3es

This modules supports the A3ES process for higher education institutions 
operating in Portgual. It allows an institution to collect and manage the 
A3ES process in a distributed manner within the institution and finally to 
submit the collected information in a single automated step.

With this module, the members of your institution can use their own 
FenixEdu application along with their own credentials local to their 
institution. This eliminates the need to directly use the A3ES web-site in 
order to fill in information for a A3ES process.


## License 

This module is provided under the AGPL Version 3 license. Please see the 
LICENSE file for more details.

![AGPL Version 3](https://www.gnu.org/graphics/agplv3-88x31.png)


## Configuring the 

In your applications configuration file set the property a3es.url accordingly 
to a value of:

```
a3es.url = http://www.a3es.pt/si/iportal.php
```

for production or to

```
a3es.url = http://testes.a3es.pt/si/iportal.php
```

for development. After your application is up and running you can indicate 
which users will manage the A3ES processes by adding them to the a3esManagers 
group.



## Extension Points

Out of the box, this module will pre-fill the information that will be 
submitted to A3ES based on information from the associated fenixedu-academic 
system it is deployed with. However each organization may have specific 
requirements or different criteria for selecting what information should be 
submitted. To help with this specialization, this module provides some 
extension points.

To implement an extension, start by creating your own implementation of the 
following interface:

```
org.fenixedu.a3es.ui.A3esFileFiller
```

Next, register your extension by calling the method:

```
org.fenixedu.a3es.ui.AccreditationProcessService.FILLERS.add
```

It is also possible to pre-fill publication information for faculty members 
in case your organization has a some sort of publications repository. In this 
case just implement the following interface:

``` 
org.fenixedu.a3es.domain.util.ScientificPublication
```
 
and set the following variable with an instance of your implementation:

``` 
org.fenixedu.a3es.domain.util.TeacherFileBean.scientificPublication
```
