2.0.10
======

Feature
-------

* Bpmn element names can be directly edited (049d80b76f2ff8b660f907210cf341781ac671eb, 0fe5c1aae73cc6a6549bd70b564c54072f27ac3b, 25d9c0b2b58b7d08d19db65d1f5e9a013a3f0453, cda96c3be0b4046230c9291c22c3739a93ddfe4c)
* Enable editing of ids and validation (8ed263605baab351c3150ae51f09ab9e38058c17, 0d2e80831527b81cff5fd5a015b763fb6a876b14)
* Flow labels can be repositioned, not resizable (251b7d1fe86c31acd3811f10c289996421b4ba4e, 036bcfc15d0c1b69b5734c76a5a65abb4630fe32)
* Message flows have labels (2a009040322c5e2b3dba1f773b386b3b574ca3a6)

Improvement
-----------

* Saving isExecutable on process as "false" per default (794c6e4ecfa230b288050a12a498d4ab1ffa4512)
* Decrease default size of subprocess (25d5cc73efac5e58e1d0b1d8c61ae90b9d0e9b2f)
* Remove missleading import warning (0396380bdc99dd977e67f1cbb8336792d7a38e61)
* Connections get moved on multi element move (f4bd3428acead69d4cf9047c15ead8de07fd3ac8)

Bugfix
------

* Task and execution listener defaults get saved (0d670f77ec19edd8586fe39e7295a5a342294198)
* Fix subprocess containment bug (14746f6d32a69d3d6649ae829a3e03345e935114)
* Fix change service task type keeps empty attribute  (c6be57452042d0bc4482e8401c1df15fdb1d44d1)
* DI information is properly saved when participant gets resized (63e7c2bbaf0fbdeb9c66292d5fcf8044c9154221)
* Fix adding lanes hiding elements on pool (0396380bdc99dd977e67f1cbb8336792d7a38e61, 539b828229727f936c4feddbb8c4bc819e414264)
* Fix boundary event connection layouting (70b1559bbc0e9989ef938d096b8fd4151cd4bf65)
* Label offset gets properly updated after multi element move (1227790ecd418de9454d85de81d5c302b3a1cfd2)
* Fix id generated twice (5ebb1cae1465dba81cdcfcd80428b36d7fa079c0)
