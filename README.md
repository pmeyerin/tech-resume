### TODOs
1. Optimize for mobile and tablet
2. Redo using assisting libraries. Possibly material-ui?
3. Get angular environments working

## To start the full project
1. Make sure Docker is running.
2. From this directory `./fullDevDeploy.sh`

Note: In this run mode UI changes in the source directory will not be picked up automatically the way Angular changes usually are.

## To run for UI development
1. In `resume.service.ts` in resume-ui change `url` from `/api` to `http://localhost:8081/api`.
2. In resume a terminal run `./deployBuildDocker.sh`
3. In resume-ui in another terminal run `ng serve`
4. In a browser open http://localhost:4200