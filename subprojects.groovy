import java.util.ArrayList;
import java.util.List;
import hudson.Util;
import hudson.model.*;
import hudson.model.Cause.UpstreamCause;
import hudson.plugins.parameterizedtrigger.BlockableBuildTriggerConfig;
import hudson.plugins.parameterizedtrigger.SubProjectsAction;

//Thread.sleep(15000)

AbstractBuild<?, ?> getDownstreamBuild(final AbstractProject<?, ?> downstreamProject,
            final AbstractBuild<?, ?> upstreamBuild) {
  
        if ((downstreamProject != null) && (upstreamBuild != null)) {
            @SuppressWarnings("unchecked")
            final List<AbstractBuild<?, ?>> downstreamBuilds = (List<AbstractBuild<?, ?>>) downstreamProject.getBuilds();
            //println downstreamBuilds
          
            for (final AbstractBuild<?, ?> innerBuild : downstreamBuilds) {
              
                println innerBuild
              
                for (final CauseAction action : innerBuild.getActions(CauseAction.class)) {
                  
                    println action
                  
                    for (final Cause cause : action.getCauses()) {
                        
                        println cause
                        if (cause instanceof UpstreamCause) {
                            
                            final UpstreamCause upstreamCause = (UpstreamCause) cause;
                            println upstreamCause.getUpstreamProject()+" "+upstreamCause.getUpstreamBuild()
                            println upstreamBuild.getProject().getFullName()+" "+upstreamBuild.getNumber()
                            if (upstreamCause.getUpstreamProject().equals(upstreamBuild.getProject().getFullName())
                                    && (upstreamCause.getUpstreamBuild() == upstreamBuild.getNumber())) {
                              
                                
                                return innerBuild;
                            }
                        }
                    }
                }
            }
        }
  
        return null;
 }

List<AbstractProject<?, ?>> getDownstreamProjects(AbstractProject<?, ?> currentProject) {
        DependencyGraph myDependencyGraph = Hudson.getInstance().getDependencyGraph();

        List<AbstractProject<?, ?>> downstreamProjectsList = new ArrayList<AbstractProject<?, ?>>();
        for (AbstractProject<?, ?> proj : myDependencyGraph.getDownstream((AbstractProject<?, ?>)currentProject)) {

        	downstreamProjectsList.add(proj);
        	downstreamProjectsList.addAll(getDownstreamProjects(proj))

        }
        return downstreamProjectsList;
 }

  List<AbstractProject<?, ?>> getAllDownstreamJobs(AbstractProject<?, ?> project)
 {
 	def currentProject = Thread.currentThread().executable.getProject();
 	def subProjList = getDownstreamProjects(currentProject);


	if (Hudson.getInstance().getPlugin("parameterized-trigger") != null) {
		for (SubProjectsAction action : Util.filter(currentProject.getActions(), SubProjectsAction.class)) {
			for (BlockableBuildTriggerConfig config : action.getConfigs()) {
				subProjList.addAll(config.getProjectList(currentProject.getParent(), null))
			}
		}
	}

	return subProjList;
 }

getAllDownstreamJobs()

if (Hudson.getInstance().getPlugin("parameterized-trigger") != null) {
    for (SubProjectsAction action : Util.filter(currentProject.getActions(), SubProjectsAction.class)) {
        for (BlockableBuildTriggerConfig config : action.getConfigs()) {
          
            for (final AbstractProject<?, ?> dependency : config.getProjectList(currentProject.getParent(), null)) {
                AbstractBuild<?, ?> returnedBuild = null;
                if (currentBuild != null) {
                    returnedBuild = getDownstreamBuild(dependency, currentBuild);
                    println dependency
                    println returnedBuild
                }
            }
        }
    }
}
