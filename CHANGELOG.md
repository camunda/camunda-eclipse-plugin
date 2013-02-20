<a name="2.0.10"></a>
# 2.0.10 (2013-02-20)

## Features

- **id:** editing possible and validation (
	[8ed26360](https://github.com/camunda/bpmn2-modeler/commit/8ed263605baab351c3150ae51f09ab9e38058c17), 
	[0d2e8083](https://github.com/camunda/bpmn2-modeler/commit/0d2e80831527b81cff5fd5a015b763fb6a876b14))
- **label:** 
  - labels can be directly edited 
  	([049d80b7](https://github.com/camunda/bpmn2-modeler/commit/049d80b76f2ff8b660f907210cf341781ac671eb), 
    [0fe5c1aa](https://github.com/camunda/bpmn2-modeler/commit/0fe5c1aae73cc6a6549bd70b564c54072f27ac3b), 
    [25d9c0b2](https://github.com/camunda/bpmn2-modeler/commit/25d9c0b2b58b7d08d19db65d1f5e9a013a3f0453), 
    [cda96c3b](https://github.com/camunda/bpmn2-modeler/commit/cda96c3be0b4046230c9291c22c3739a93ddfe4c))
	- flow labels can be repositioned 
	  ([251b7d1f](https://github.com/camunda/bpmn2-modeler/commit/251b7d1fe86c31acd3811f10c289996421b4ba4e), 
	  [036bcfc1](https://github.com/camunda/bpmn2-modeler/commit/036bcfc15d0c1b69b5734c76a5a65abb4630fe32))
	- message flows have labels 
		([2a009040](https://github.com/camunda/bpmn2-modeler/commit/2a009040322c5e2b3dba1f773b386b3b574ca3a6))
	- labels are not resizable

## Improvements

- **export:** isExecutable in saved xml is "false" per default
	([794c6e4e](https://github.com/camunda/bpmn2-modeler/commit/794c6e4ecfa230b288050a12a498d4ab1ffa4512))
- **visual:** default size of subprocess decreased
	([25d5cc73](https://github.com/camunda/bpmn2-modeler/commit/25d5cc73efac5e58e1d0b1d8c61ae90b9d0e9b2f))
- **import:** missleading import warning removed
	([0396380b](https://github.com/camunda/bpmn2-modeler/commit/0396380bdc99dd977e67f1cbb8336792d7a38e61))
- **move:** connections get moved on multi element move
	([f4bd3428](https://github.com/camunda/bpmn2-modeler/commit/f4bd3428acead69d4cf9047c15ead8de07fd3ac8))

## Bug Fixes

- **subprocess:** fix containment bug
	([14746f6d](https://github.com/camunda/bpmn2-modeler/commit/14746f6d32a69d3d6649ae829a3e03345e935114))
- **export:**
	- remove empty attribute after service task type change 
	  ([c6be5745](https://github.com/camunda/bpmn2-modeler/commit/c6be57452042d0bc4482e8401c1df15fdb1d44d1))
  - di information is properly saved when participant gets resized 
	  ([63e7c2bba](https://github.com/camunda/bpmn2-modeler/commit/63e7c2bbaf0fbdeb9c66292d5fcf8044c9154221))
  - task and execution listener defaults get saved 
	  ([0d670f77](https://github.com/camunda/bpmn2-modeler/commit/0d670f77ec19edd8586fe39e7295a5a342294198))
- **lane:** fix adding hides elements on pool 
	([0396380b](https://github.com/camunda/bpmn2-modeler/commit/0396380bdc99dd977e67f1cbb8336792d7a38e61), 
	[539b8282](https://github.com/camunda/bpmn2-modeler/commit/539b828229727f936c4feddbb8c4bc819e414264))
- **boundary event:**
	- connection layouting 
	  ([70b1559b](https://github.com/camunda/bpmn2-modeler/commit/70b1559bbc0e9989ef938d096b8fd4151cd4bf65))
  - fix offset when attached to activity
	  ([0d670f77](https://github.com/camunda/bpmn2-modeler/commit/0d670f77ec19edd8586fe39e7295a5a342294198))
- **label:** offset gets properly updated after multi element move 
	([1227790e](https://github.com/camunda/bpmn2-modeler/commit/1227790ecd418de9454d85de81d5c302b3a1cfd2))
- **id:** fix id generated twice 
	([5ebb1cae](https://github.com/camunda/bpmn2-modeler/commit/5ebb1cae1465dba81cdcfcd80428b36d7fa079c0))
