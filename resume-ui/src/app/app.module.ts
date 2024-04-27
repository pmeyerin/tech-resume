import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {CdkAccordionModule} from "@angular/cdk/accordion";
import { UserDisplayComponent } from './user-display/user-display.component';
import { SkillsOverviewComponent } from './skills-overview/skills-overview.component';
import {MatChipsModule} from "@angular/material/chips";
import { WorkHistoryComponent } from './work-history/work-history.component';
import {MatTabsModule} from "@angular/material/tabs";
import { RootNavComponent } from './root-nav/root-nav.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import {MatExpansionModule} from "@angular/material/expansion";
import {MatCardModule} from "@angular/material/card";
import { CatDashComponent } from './cat-dash/cat-dash.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatMenuModule } from '@angular/material/menu';
import { HttpClientModule } from "@angular/common/http";
import { ProjectParentComponent } from './project-parent/project-parent.component';
import { CategorySectionComponent } from './category-section/category-section.component';
import {MatDialogModule} from "@angular/material/dialog";
import { SkillsDialogComponent } from './skills-dialog/skills-dialog.component';
import { SpinnerDialogComponent } from './spinner-dialog/spinner-dialog.component';
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";

@NgModule({
  declarations: [
    AppComponent,
    UserDisplayComponent,
    SkillsOverviewComponent,
    RootNavComponent,
    CatDashComponent,
    ProjectParentComponent,
    CategorySectionComponent,
    SkillsDialogComponent,
    WorkHistoryComponent,
    SpinnerDialogComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    CdkAccordionModule,
    MatChipsModule,
    MatTabsModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatExpansionModule,
    MatCardModule,
    MatGridListModule,
    MatMenuModule,
    HttpClientModule,
    MatDialogModule,
    MatTooltipModule,
    MatProgressSpinnerModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
