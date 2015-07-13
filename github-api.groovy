import org.kohsuke.github.*

def thr = Thread.currentThread(); 
def build = thr?.executable;
def oauthAccessToken = '';
  
GitHub github = GitHub.connectUsingOAuth(oauthAccessToken);
GHRepository repo = github.getRepository('scapewang/hexo-theme-air');

println repo.getFullName()

def compare = repo.getCompare('hustcer:master', 'scapewang:master')

def files = compare.getFiles()

files.each { file ->
	println file.getFileName()
}