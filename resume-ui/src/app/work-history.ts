import {ProjectParent} from "./project-parent";

export interface WorkHistory {
  workerId: string;
  workerName: string;
  workerPhone: string;
  workerEmail: string;
  projects: ProjectParent[];
}
