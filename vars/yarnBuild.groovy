def call(String buildMode = "build", Closure body) {
  def label = "nodejs-${repoOwner}"
  def podYaml = libraryResource 'podtemplates/nodejs/pod.yml'
  podTemplate(name: 'nodejs', label: label, yaml: podYaml, podRetention: always(), idleMinutes: 30) {
    node(label) {
      body()
      if(env.BRANCH_NAME != "master") {
        buildMode = "build:dev" 
      }
      container('nodejs') {
        sh """
          mkdir -p app
          cd ./app
          yarn install
          yarn run $buildMode
        """
      }
      stash name: "app", includes: "app/**" 
    }
  }
}
