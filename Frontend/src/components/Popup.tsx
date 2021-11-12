export default function Popup(props: any) {
    return (
        <div className={`${props.shouldopen ? 'fixed' : 'hidden'} w-screen h-screen bg-black bg-opacity-30 left-0 top-0 z-10`}>
            <div className="absolute mx-auto top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 p-4 rounded-lg bg-white">
                <div className="w-full flex items-center justify-between border-b mb-8">
                    <p> {props.title} </p>
                    <svg onClick={props.toggle} className="p-2 cursor-pointer" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="40" height="40"><path fill="none" d="M0 0h24v24H0z"/><path d="M12 10.586l4.95-4.95 1.414 1.414-4.95 4.95 4.95 4.95-1.414 1.414-4.95-4.95-4.95 4.95-1.414-1.414 4.95-4.95-4.95-4.95L7.05 5.636z"/></svg>
                </div>
                <div {...props} >
                </div>
            </div>
        </div>
        
    );
}