# 2.2.0

- Highlight plugin prefix in buildplan:list ([1436b5f](https://github.com/mojohaus/buildplan-maven-plugin/commit/1436b5f188086c2def07af7a9b9becbd5d121b19))
- Reorder columns order for list-plugin goal ([6cc881e](https://github.com/mojohaus/buildplan-maven-plugin/commit/6cc881eb2d18c27dc761fd75e6bbaefdeb23b01e))
- Reorder columns order for list-phase goal ([e92e69d](https://github.com/mojohaus/buildplan-maven-plugin/commit/e92e69d1d2f4404ea1086d1f3dacc500601cda12))
- Display plugins version ([822d7fa](https://github.com/mojohaus/buildplan-maven-plugin/commit/822d7fa72eec21801679ae0aeb2e6032a9b9fb8c)) by [@nhumblot](https://github.com/nhumblot) and [@welfoz](https://github.com/welfoz)
- Rename ID column to EXECUTION ID ([b32ed85](https://github.com/mojohaus/buildplan-maven-plugin/commit/b32ed8549990512698cb2896a0e5ae70dc5ab5b3)) by [@hboutemy](https://github.com/hboutemy)
- Rename last column PLUGIN to ID in HTML report ([da3befb](https://github.com/mojohaus/buildplan-maven-plugin/commit/da3befbe5cbdda3121a3157d2885fc31912e8754)) by [@hboutemy](https://github.com/hboutemy)

# 2.1.0

- Add a :report goal to generate plugin executions list in HTML ([86b2717](https://github.com/mojohaus/buildplan-maven-plugin/commit/86b2717deaf3212b72d48f604f55012d38930e08))
- Reorder table columns order for list goal ([6b21c97](https://github.com/mojohaus/buildplan-maven-plugin/commit/6b21c97937f704b5d03a7846ad22de07a5bf565f))

# 2.0.0

- Add missing documentation on showLifecycles and showAllPhases ([d6d115a](https://github.com/mojohaus/buildplan-maven-plugin/commit/d6d115ada8e83a96ee54432a4f103e6bde284e62))
- Switch to MojoHaus ([878bc59](https://github.com/mojohaus/buildplan-maven-plugin/commit/878bc59f10d3970b349dd88600c1450fcb3af589)) by [@hboutemy](https://github.com/hboutemy)
- Activate Reproducible Builds ([a9eba7e](https://github.com/mojohaus/buildplan-maven-plugin/commit/a9eba7e88e0f382fe7ef12227dcecb11fa907d09)) by [@hboutemy](https://github.com/hboutemy)

# 1.5

- Hide empty line from empty phase ([f3cc6c8](http://github.com/mojohaus/buildplan-maven-plugin/commit/f3cc6c83614171b8fc4627a79b97958eb47cab06))
- Report ITs in code coverage ([354101b](http://github.com/mojohaus/buildplan-maven-plugin/commit/354101b4ce40f6c7a7ada4cd88cc25ad3e45a43a)) by [@malice00](https://github.com/malice00)
- Add options showLifecycles and showAllPhases ([45c2cd9](http://github.com/mojohaus/buildplan-maven-plugin/commit/45c2cd9be647193dacb0f3607a78cc6bca069fe3)) by [@malice00](https://github.com/malice00)

# 1.4

***

- Migrate to Java 8 ([724e6fa](http://github.com/mojohaus/buildplan-maven-plugin/commit/724e6faa41ad555ea1fb8529552232b6464ccec2))
- Add 'skip' parameter to bypass execution ([c443a6a](http://github.com/mojohaus/buildplan-maven-plugin/commit/c443a6add0930ca3d818f1aac6624d68b4105872)) by [@stdx](https://github.com/stdx)

# 1.3

***

- Ensure thread safety when writing output to file ([92a248a](http://github.com/mojohaus/buildplan-maven-plugin/commit/92a248a4e91bbe2258241eca28e4e06ef21c676b))
- Align table header separators when using list goal ([9ea43b8](http://github.com/mojohaus/buildplan-maven-plugin/commit/9ea43b892d4d47742a5095c3eaeb3a793c270b9e))
- Add Integration Tests ([9d7f91d](http://github.com/mojohaus/buildplan-maven-plugin/commit/9d7f91d5651121b1b0026279e3aba60134946eb8))
- Introduce output file property ([94286aa](http://github.com/mojohaus/buildplan-maven-plugin/commit/94286aaa1806ce1a2614ccb77892442a539c02dd)) by [@mattnelson](https://github.com/mattnelson)
- Build project with maven toolchains to ensure JRE 6 compatibility ([f285db4](http://github.com/mojohaus/buildplan-maven-plugin/commit/f285db4d57c479dd4b7771eaa3121f53d7b84ec8))
- Fix README formatting ([f7e88fc](http://github.com/mojohaus/buildplan-maven-plugin/commit/f7e88fc49680344f46dfb8486c8680edbfd5018b))
- Correctly generate Introduction and Usage site pages ([9937ea6](http://github.com/mojohaus/buildplan-maven-plugin/commit/9937ea6774f546d5c407123d200c38db369b81cc))

# 1.2

***

- Respect maven lifecycle sequence in report ([e620021](http://github.com/mojohaus/buildplan-maven-plugin/commit/e620021c7af22db9f6fe10115ea2b76ca3177230))
- Display build plan in one operation ([56879cb](http://github.com/mojohaus/buildplan-maven-plugin/commit/56879cbc1a908bb5c46dc934b2aa216cdd4f9b4f))
- Report correct phase ([68a93d0](http://github.com/mojohaus/buildplan-maven-plugin/commit/68a93d09eb7488a42f8fc08a97c48d77c8ed4c38))

