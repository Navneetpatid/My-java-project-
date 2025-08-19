Your requirement in simple terms:

1. You already have a working pipeline repository (let’s call it Pipeline-Repo).


2. You also have another repository (CER-Repo) which has many APIs.


3. In the pipeline, you want to:

Generate a token (using an API from CER-Repo).

Use that token to call other APIs in CER-Repo.

Send parameters from the pipeline into those API calls.

Save the data in the DB (through the APIs).

	
