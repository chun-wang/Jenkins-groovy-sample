import hudson.model.*

Properties ReadPropertiesFromFile(String path){
	Properties props = new Properties()
	File propsFile = new File(path)
	props.load(propsFile.newDataInputStream())

	return props;
}

void AddJobParameter(List<StringParameterValue> paralist) {
	def paraAction = new ParametersAction(paralist)
	Thread.currentThread().executable.addAction(paraAction)
}

List<StringParameterValue> list = [];
Properties props = ReadPropertiesFromFile('/home/jenkins/tools/env.properties')

def build = Thread.currentThread().executable
def actionList = build.getActions(ParametersAction)
println actionList.size()
def pA = actionList.get(0)
def paraValues = pA.getParameters()

paraValues.each { param ->
	props.setProperty(param.getName(), (String)param.getValue()) 
}

Enumeration enum1 = props.propertyNames();
while(enum1.hasMoreElements()) {
	String strKey = (String) enum1.nextElement();
 	String strValue = props.getProperty(strKey);
	println(strKey + "=" + strValue);

	list.add(new StringParameterValue(strKey, strValue))
}

AddJobParameter(list)

