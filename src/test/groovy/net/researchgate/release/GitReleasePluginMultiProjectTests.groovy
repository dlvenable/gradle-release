package net.researchgate.release

import org.gradle.api.GradleException
import org.gradle.testfixtures.ProjectBuilder

@Mixin(PluginHelper)
class GitReleasePluginMultiProjectTests extends GitSpecification {
    def setup() {
        final subproject = new File(localGit.repository.getWorkTree(), "subproject")
        project = ProjectBuilder.builder().withName("GitReleasePluginTest").withProjectDir(subproject).build()
        project.apply plugin: ReleasePlugin
    }

    def "subproject should work with git beeing in parentProject"() {
        when:
        project.checkUpdateNeeded.execute()
        then:
        noExceptionThrown()
    }

    def '`checkUpdateNeeded` should detect remote changes to pull in subproject'() {
        given:
        gitAddAndCommit(remoteGit, 'gradle.properties') { it << '222' }
        when:
        project.checkUpdateNeeded.execute()
        then:
        GradleException ex = thrown()
        ex.cause.message.contains "You have 1 remote change(s) to pull."
    }
}
