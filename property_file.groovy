Properties props = new Properties()
File propsFile = new File('/usr/local/etc/test.properties')
props.load(propsFile.newDataInputStream())

println props.getProperty('porcupine')

Integer rand = new Random().next(4)
props.setProperty('porcupine', rand.toString())
props.store(propsFile.newWriter(), null)

props.load(propsFile.newDataInputStream())
println props.getProperty('porcupine')