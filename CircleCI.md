## Circle CI Pipeline 

- Create a directory called .circleci in the root directory of your local GitHub or Bitbucket code repository.
- Add [config.yml][config.yml] file in the `.circleci` directory with the following lines that import the [hello-build orb][hello-build.orb].

  ```bash
	version: 2.1

	orbs:
	    hello: circleci/hello-build@0.0.7 # uses the circleci/buildpack-deps Docker image

	workflows:
	    "Hello Workflow":
	        jobs:
	          - hello/hello-build    
  ```

- Find the list of Supported images @ https://circleci.com/docs/2.0/docker-image-tags.json


---

[config.yml]: https://circleci.com/docs/2.0/configuration-reference/
[hello-build.orb]: https://circleci.com/orbs/registry/orb/circleci/hello-build