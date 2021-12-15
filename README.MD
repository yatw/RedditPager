  
  # Reddit Pager

  
Project to demonstrate how to use reddit api in Android and how to use Pager3 library

  

## Set up reddit credential

1. Register an application at: [https://www.reddit.com/prefs/apps](https://www.reddit.com/prefs/apps)
2. Create an installed app **Not web app**, you should get a client id
3. Create a file `config_dev.properties` same level as app.
4. Place your client id:
```
REDDIT_CLIENT_ID=xxxxxx
```

## Steps
1. Define PagingSource, basically a callback we need to implement
	* has a load method that make the api call in service
	* has a getRefreshKey method, define what key (page) to start displaying
2. Use the PagingSource in Repository, turn it into a flow (observable)
	* Pager takes in config and pagingSourceFactory
	* click into config to see the available paramter
	* placeholder is only use if maxSize is set
	* if maxSize is set too small, and you scroll too fast, weird stuff start happening,
		* it keeps calling the same page
		* sometime crash because it is trying to drop some pages already dropped
3. Define the PagingData flow in ViewModel
	* Use the operator .cachedIn(viewModelScope) to avoid re-fetching on screen rotation
4. Use a recyclverview with PagingDataAdapter
	* different is PagingDataAdaptertakes in a diffCallback in constructor
	* It is for pager3 to handle refresh the whole list, for calculating the differences between two lists. It uses an algorithm to figure out the minimum number of changes to make from the old list to produce the new list.
		* areItemsTheSame check if it is the same item (same property)
		* areContentsTheSame check if the equal item have the same content (for ui update)
5. Collect the flow in fragment
	* There is a separate loadStateFlow from PagingDataAdapter that give us the loading state of the pagingData, we can observe that for make error view/loading view invisible
6. To add a header/footer
	* Create a separate Adapter that extends LoadStateAdapter and use that when setting the adapter in the recyclver view in fragment
7. Add separator between the items
	* use the insertSeparators transformation from PagingData to decide if we want to insert a item into between the list, the PagingDataAdapter will need to handle this view type
8. Use RemoteMediator to support loading data from local database instead. And delegate the network fetching logic to this mediator
	* database become the source of truth
		* Return PagingSource directly from Dao
	* implement the load method from RemoteMediator
		* PagingState - this gives us information about the pages that were loaded before, the most recently accessed index in the list, and the PagingConfig we defined when initializing the paging stream.
		* LoadType - this tells us whether we need to load data at the end (LoadType.APPEND) or at the beginning of the data (LoadType.PREPEND) that we previously loaded, or if this the first time we're loading data (LoadType.REFRESH).
		* Basically use the LoadType to find the correct page to load and save the items related to this key in db
		* associate every item with a prev key and next key in table
		* If no more item to load return MediatorResult.Success(endOfPaginationReached = true)
		* If no more item to load return MediatorResult.Success(endOfPaginationReached = true)
	
