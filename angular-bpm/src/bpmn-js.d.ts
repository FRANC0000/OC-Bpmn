declare module 'bpmn-js/lib/Modeler' {
  interface SaveXMLResult {
    xml: string;
  }

  interface ImportXMLResult {
    warnings: any[];
  }

  class Modeler {
    constructor(options: { container: HTMLElement; keyboard?: { bindTo: Document } });
    importXML(xml: string): Promise<ImportXMLResult>;
    saveXML(options: { format: boolean }): Promise<SaveXMLResult>;
    destroy(): void;
  }

  export default Modeler;
}
