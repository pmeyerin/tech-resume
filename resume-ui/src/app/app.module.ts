import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { UserDisplayComponent } from './user-display/user-display.component';
import { ViewsDisplayComponent } from './views-display/views-display.component';
import { CatViewComponent } from './cat-view/cat-view.component';
import { ChronViewComponent } from './chron-view/chron-view.component';
import { RouterOutlet } from "@angular/router";
import { AppRoutingModule } from "./app-routing.module";
import { ProjectCardComponent } from './project-card/project-card.component';
import { ProjectParentCardComponent } from './project-parent-card/project-parent-card.component';
import { SkillsListComponent } from './skills-list/skills-list.component';
import { ChronCardComponent } from './chron-card/chron-card.component';
import { WorkerSkillsSectionComponent } from './worker-skills-section/worker-skills-section.component';
import { WorkHistorySectionComponent } from './work-history-section/work-history-section.component';
import { CategoryCardComponent } from './category-card/category-card.component';
import { HttpClientModule } from '@angular/common/http';
import { SkillDialogComponent } from './skill-dialog/skill-dialog.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from "@angular/router";
import { ModalComponent } from './modal/modal.component';
import { TooltipComponent } from './tooltip/tooltip.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    UserDisplayComponent,
    ViewsDisplayComponent,
    CatViewComponent,
    ChronViewComponent,
    ProjectCardComponent,
    ProjectParentCardComponent,
    SkillsListComponent,
    ChronCardComponent,
    WorkerSkillsSectionComponent,
    WorkHistorySectionComponent,
    CategoryCardComponent,
    SkillDialogComponent,
    ModalComponent,
    TooltipComponent
  ],
  imports: [
      BrowserModule,
      AppRoutingModule,
      RouterOutlet,
      HttpClientModule,
      BrowserAnimationsModule,
      RouterModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
