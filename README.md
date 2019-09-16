Solution to the following assignment:


## Assignment

Create a simple one-activity app which displays the weather for the users' location. 

### Design and Architecture
There is no big emphasis on visual design, but thought should be taken into account regarding overall architecture such as design-patterns and the Android lifecycle.

Similarly, there are no requirements as to what architecture you should choose, but we recommend you choose the architecture you're most comfortable with. 

### Third-party Libraries
Use of third-party libraries should be kept to a minimum (including Architecture Components), but you may use for example `OkHttp` to perform the required network requests. 

### Delivery
Once finished, deliver the result of the assignment either as project available on an open-source hosting provider such as GitHub or Bitbucket or as a zip file of the entire project.


## Weather API
To access the weather, you can use the following API:

```http
https://api.darksky.net/forecast/2bb07c3bece89caf533ac9a5d23d8417/latitude,longitude
````

### The Project

- MVVM with ViewModel and LiveData
- Retrofit/Moshi
- Unit tests with Mockito
