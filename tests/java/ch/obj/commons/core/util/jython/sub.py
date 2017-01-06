
# - for a first manual test:
# cd /dist
# java -jar jython-standalone.jar
# execfile('../tests/java/ch/obj/commons/core/util/jython/sub.py')


from ch.obj.commons.core.util.jython import JythonSup
   
class Sub(JythonSup):

    def callSuperMethods(self):
        self.publicFinal('foo')
        self.publicNonfinal('bar')
        self.super__protectedFinal('baz') # classic
        self.protectedFinal('baz')
        self.protectedNonfinal('tar')

mySub = Sub()
mySub.callSuperMethods()
