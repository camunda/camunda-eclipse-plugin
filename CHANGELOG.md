<a name="2.0.12"></a>
# 2.0.12 (2013-04-04)

## Features

- **canvas:**
  - add correct event subprocess shapes / fix update handling
    ([1ebde976](https://github.com/camunda/camunda-modeler/commit/1ebde976dc2e94be39955bca9485c5c36f5c6181))
  - switch to black and white colors 
    ([920afee2](https://github.com/camunda/camunda-modeler/commit/920afee20b19daa30c2f12cd5a3edff930da5971), 
    [c1fadef0](https://github.com/camunda/camunda-modeler/commit/c1fadef01d8e8b0737049a1e75c44e690fc490a4))
  - hide grid
    ([53884135](https://github.com/camunda/camunda-modeler/commit/538841351687a3c7fe75c959c2071ebfb0f1cbeb))
  - add scroll padding via invisible scroll shape
    ([4a3271ac](https://github.com/camunda/camunda-modeler/commit/4a3271acbc3d478fa6898213cc19aea4ef5fc82f),
    [c16a3b11](https://github.com/camunda/camunda-modeler/commit/c16a3b11faf917b1034ab3933a6200595f1b3725),
    [ebf059b3](https://github.com/camunda/camunda-modeler/commit/ebf059b3948aff8c90f5cb70d9de8646d8ef3203),
    [f501b18a](https://github.com/camunda/camunda-modeler/commit/f501b18a7109503403b95b702a127ee4d8003ce2),
    [78d121af](https://github.com/camunda/camunda-modeler/commit/78d121afadddbfdf11630835c219c02d0c5e5191), 
    [725b38f1](https://github.com/camunda/camunda-modeler/commit/725b38f147691397b86904c5c7afa55a53d98a1b), 
    [3a1fdc4e](https://github.com/camunda/camunda-modeler/commit/3a1fdc4e83e92752369b75d1b833793ade197595),
    [85654b1f](https://github.com/camunda/camunda-modeler/commit/85654b1f26b0eb24a9f0a989c58544d72af584e8))
- **boundary event:**
  ([592fd522](https://github.com/camunda/camunda-modeler/commit/592fd5220f527277c8bace2f4a1b83d0965bc554), 
  [1d5e7e7e](https://github.com/camunda/camunda-modeler/commit/1d5e7e7ef26b744518defa12f00646d18be54e4e))
  - add snap to attached shape
  - add reposition upon attached shape resize
- **property panels:**
  - add trigger by event flag
    ([1ebde976](https://github.com/camunda/camunda-modeler/commit/1ebde976dc2e94be39955bca9485c5c36f5c6181))
- **code structure:**
  - rename / unify package and artifact names
    ([a4fa62e7](https://github.com/camunda/camunda-modeler/commit/a4fa62e7791c5ed36791deb66a9d6dcc35e9e1d8),
    [75136a4f](https://github.com/camunda/camunda-modeler/commit/75136a4f11f14ce015a34a902f054e49ffc10715),
    [9d239d67](https://github.com/camunda/camunda-modeler/commit/9d239d67fdb2436376537baf21eab3176f38b725),
    [e456a232](https://github.com/camunda/camunda-modeler/commit/e456a2326d818f1939d0a2de641056e456867f0c),
    [b9a4ce74](https://github.com/camunda/camunda-modeler/commit/b9a4ce741ba1c937bead527be8c757579a734cde))
- **resize**:
  - add child element bounding box check to participants and lanes 
    ([ce82355a](https://github.com/camunda/camunda-modeler/commit/ce82355a8a233068243de4850fec38f6cbaafd90))
  - add participant resize with lanes in correct direction
    ([ce82355a](https://github.com/camunda/camunda-modeler/commit/ce82355a8a233068243de4850fec38f6cbaafd90),
    [4a50bf9e](https://github.com/camunda/camunda-modeler/commit/4a50bf9eae2a74fa4bdbf44e27ff729b9e1d8e5e),
    [f13fd398](https://github.com/camunda/camunda-modeler/commit/f13fd398e19072a02a24225c3da278452d5cd106))

## Improvements

- **create diagram:**
  - cleanup naming in wizard
    ([45d78e06](https://github.com/camunda/camunda-modeler/commit/45d78e0642c691a8d087a1e50c427008e16ad21b))
  - update bpmn diagram categories and hook into quick create lists
    ([1394f662](https://github.com/camunda/camunda-modeler/commit/1394f662271b587a75e7d31fe8676e95ab2f25db), 
    [b79c2bfb](https://github.com/camunda/camunda-modeler/commit/b79c2bfbb650fa70a7fa979336f69f4dccc2da05))
  - open property panels after creation
    ([b3ca6427](https://github.com/camunda/camunda-modeler/commit/b3ca6427bd67b0b9c68d6492cd3ec5dc96dcaeb1))
- **layout:**
  - check boundary event and attached to bounds when computing overlapping bendpoints after move
    ([1ebde976](https://github.com/camunda/camunda-modeler/commit/1ebde976dc2e94be39955bca9485c5c36f5c6181))
- **move:**
  - correcty move sequence flows and element labels on container resize
    ([8db64700](https://github.com/camunda/camunda-modeler/commit/8db64700f5b415df5ea6dfc0a17e35740171844a))
  - disable drop on connections for existing flow nodes
    ([949f6ce4](https://github.com/camunda/camunda-modeler/commit/949f6ce4cd1c79632cad7040d9edfe448e640f89))
- **add:**
  - connection split with flow node works properly
    ([2492e5e0](https://github.com/camunda/camunda-modeler/commit/2492e5e0de7fb5308bddd81682ccf057117dac7b), 
    [dcb3545d](https://github.com/camunda/camunda-modeler/commit/dcb3545dda8ec18260b35fd1cc03dfadebbd3bc5))

## Bug Fixes

- **import:**
  - fix initial label position
    ([5cd6796c](https://github.com/camunda/camunda-modeler/commit/5cd6796c5232a98ac6863e5f6c13fca76cdf21f5))
- **export:**
  - image generates properly with spaces in diagram file name 
    ([3884ec18](https://github.com/camunda/camunda-modeler/commit/3884ec18659fa69814e4f52b4c5b552bc7704c3c))
- **layout:**
  - fix infinite loop regression on boundary event layout after move
    ([3e05e1dc](https://github.com/camunda/camunda-modeler/commit/3e05e1dc2c8efd1efd693ad97d8ac12f638f3972))
  - fix connection layouting if bendpoint is close to shape
    ([9bf366dd](https://github.com/camunda/camunda-modeler/commit/9bf366dd8ccb67f14f3fc6d1a552ef3bb6e55629))
- **canvas:**
  - fix infinite loop when adding message flow via palette
    ([2b760e57](https://github.com/camunda/camunda-modeler/commit/2b760e57997b01499890581fe75cee0d512e41da), 
    [9e77673b](https://github.com/camunda/camunda-modeler/commit/9e77673b6f619af4a21afda96ce2b58ba257c3ad))
- **property panels:**
  - suppress disposed warnings
    ([9fd48de3](https://github.com/camunda/camunda-modeler/commit/9fd48de3ec869eafb65f209c3e6b587968bff237))

<a name="2.0.11"></a>
# 2.0.11 (inofficial)

<a name="2.0.10"></a>
# 2.0.10 (2013-02-20)

## Features

- **id:** editing possible and validation (
  [8ed26360](https://github.com/camunda/camunda-modeler/commit/8ed263605baab351c3150ae51f09ab9e38058c17), 
  [0d2e8083](https://github.com/camunda/camunda-modeler/commit/0d2e80831527b81cff5fd5a015b763fb6a876b14))
- **label:** 
  - labels can be directly edited 
    ([049d80b7](https://github.com/camunda/camunda-modeler/commit/049d80b76f2ff8b660f907210cf341781ac671eb), 
    [0fe5c1aa](https://github.com/camunda/camunda-modeler/commit/0fe5c1aae73cc6a6549bd70b564c54072f27ac3b), 
    [25d9c0b2](https://github.com/camunda/camunda-modeler/commit/25d9c0b2b58b7d08d19db65d1f5e9a013a3f0453), 
    [cda96c3b](https://github.com/camunda/camunda-modeler/commit/cda96c3be0b4046230c9291c22c3739a93ddfe4c))
  - flow labels can be repositioned 
    ([251b7d1f](https://github.com/camunda/camunda-modeler/commit/251b7d1fe86c31acd3811f10c289996421b4ba4e), 
    [036bcfc1](https://github.com/camunda/camunda-modeler/commit/036bcfc15d0c1b69b5734c76a5a65abb4630fe32))
  - message flows have labels 
    ([2a009040](https://github.com/camunda/camunda-modeler/commit/2a009040322c5e2b3dba1f773b386b3b574ca3a6))
  - labels are not resizable

## Improvements

- **export:** isExecutable in saved xml is "false" per default
  ([794c6e4e](https://github.com/camunda/camunda-modeler/commit/794c6e4ecfa230b288050a12a498d4ab1ffa4512))
- **visual:** default size of subprocess decreased
  ([25d5cc73](https://github.com/camunda/camunda-modeler/commit/25d5cc73efac5e58e1d0b1d8c61ae90b9d0e9b2f))
- **import:**
  - missleading import warning removed
    ([0396380b](https://github.com/camunda/camunda-modeler/commit/0396380bdc99dd977e67f1cbb8336792d7a38e61))
  - editor opens with current import result, even if unrecoverable error occurs
    ([0c519c18](https://github.com/camunda/camunda-modeler/commit/0c519c184247ea3b4a74b9e4729d1c5fbc312976]))
- **move:** connections get moved on multi element move
  ([f4bd3428](https://github.com/camunda/camunda-modeler/commit/f4bd3428acead69d4cf9047c15ead8de07fd3ac8))

## Bug Fixes

- **subprocess:** fix containment bug
  ([14746f6d](https://github.com/camunda/camunda-modeler/commit/14746f6d32a69d3d6649ae829a3e03345e935114))
- **export:**
  - remove empty attribute after service task type change 
    ([c6be5745](https://github.com/camunda/camunda-modeler/commit/c6be57452042d0bc4482e8401c1df15fdb1d44d1))
  - di information is properly saved when participant gets resized 
    ([63e7c2bba](https://github.com/camunda/camunda-modeler/commit/63e7c2bbaf0fbdeb9c66292d5fcf8044c9154221))
  - task and execution listener defaults get saved 
    ([0d670f77](https://github.com/camunda/camunda-modeler/commit/0d670f77ec19edd8586fe39e7295a5a342294198))
- **lane:** fix adding hides elements on pool 
  ([0396380b](https://github.com/camunda/camunda-modeler/commit/0396380bdc99dd977e67f1cbb8336792d7a38e61), 
  [539b8282](https://github.com/camunda/camunda-modeler/commit/539b828229727f936c4feddbb8c4bc819e414264))
- **boundary event:**
  - connection layouting 
    ([70b1559b](https://github.com/camunda/camunda-modeler/commit/70b1559bbc0e9989ef938d096b8fd4151cd4bf65))
  - fix offset when attached to activity
    ([0d670f77](https://github.com/camunda/camunda-modeler/commit/0d670f77ec19edd8586fe39e7295a5a342294198))
- **label:** offset gets properly updated after multi element move 
  ([1227790e](https://github.com/camunda/camunda-modeler/commit/1227790ecd418de9454d85de81d5c302b3a1cfd2))
- **id:** fix id generated twice 
  ([5ebb1cae](https://github.com/camunda/camunda-modeler/commit/5ebb1cae1465dba81cdcfcd80428b36d7fa079c0))

## Know Issues

- **property panel** enter in text field will trigger focus on first control in tab (most often the id field)
  occurs only in eclipse juno 4.2, is fixed in eclipse kepler 4.3
