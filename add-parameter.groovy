import hudson.model.*

def pa = new ParametersAction([
  new StringParameterValue("REPO_ADD", "https://github.com/")
])

// add variable to current job
Thread.currentThread().executable.addAction(pa)